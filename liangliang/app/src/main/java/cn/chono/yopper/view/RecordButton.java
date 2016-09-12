package cn.chono.yopper.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.IOException;

import cn.chono.yopper.R;


@SuppressLint("NewApi")
public class RecordButton extends TextView {

    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSavePath(String path) {
        mFileName = path;
    }

    public String getSavePath() {
        return mFileName;
    }

    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }

    private String mFileName = null;

    private OnFinishedRecordListener finishedListener;

    private static final int MIN_INTERVAL_TIME = 2000;// 2s
    private long startTime;


    /**
     * 取消语音发送
     */
    private Dialog recordIndicator;

    private static int[] res = {R.drawable.mic1, R.drawable.mic2,
            R.drawable.mic3, R.drawable.mic4, R.drawable.mic5, R.drawable.mic6};

    private View view;
    private ImageView soundWave;
    private TextView counter_tv;
    private TextView tips_tv;

    private MediaRecorder recorder;

    private ObtainDecibelThread thread;

    private Handler volumeHandler;
    static final int ONE_MINUTE = 1000;
    static final int SOUND_ANIM = 0;
    boolean isSendConfirm = true;
    public final static int MAX_TIME = 60;//一分钟

    private void init() {
        volumeHandler = new ShowVolumeHandler();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int[] btnLoc = new int[2];
        getLocationInWindow(btnLoc);

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setText(R.string.recorde_up_str);
                initDialogAndStartRecord();
                volumeHandler.sendEmptyMessage(ONE_MINUTE);
                break;
            case MotionEvent.ACTION_UP:
                this.setText(R.string.recorde_press_str);
                volumeHandler.removeMessages(ONE_MINUTE);
                if (isSendConfirm) {
                    finishRecord();
                } else {
                    cancelRecord();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                LogUtils.e("Move:" + btnLoc[1] + "---getY:" + event.getY());

                if (event.getY() < -200) {
                    tips_tv.setText("松开手指，取消发送");
                    isSendConfirm = false;
                } else {
                    tips_tv.setText("手指上滑，取消发送");
                    isSendConfirm = true;
                }

                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                cancelRecord();
                Toast.makeText(getContext(), "取消发送", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initDialogAndStartRecord() {

        startTime = System.currentTimeMillis();
        recordIndicator = new Dialog(getContext(), R.style.sound_dialog_style);
        view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sound,null);
        soundWave = (ImageView)view.findViewById(R.id.sound_wave);
        tips_tv = (TextView)view.findViewById(R.id.sound_tips);
        counter_tv = (TextView)view.findViewById(R.id.sound_counter);

        recordIndicator.setOnDismissListener(onDismiss);
//        WindowManager.LayoutParams lp = recordIndicator.getWindow().getAttributes();
//        lp.gravity = Gravity.CENTER;


        Window window = recordIndicator.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        recordIndicator.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        recordIndicator.setContentView(view);

        isSent = false;
        startRecording();
        recordIndicator.show();
    }

    boolean isSent = false;

    private void finishRecord() {
        if (isSent) {
            return;
        }
        stopRecording();
        recordIndicator.dismiss();
        isSent = true;
        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), "时间太短！", Toast.LENGTH_SHORT).show();
            File file = new File(mFileName);
            file.delete();
            return;
        }

        if (finishedListener != null)
            finishedListener.onFinishedRecord(mFileName, (int) (intervalTime / 1000));
    }

    private void cancelRecord() {
        stopRecording();
        recordIndicator.dismiss();

        Toast.makeText(getContext(), "取消录音！", Toast.LENGTH_SHORT).show();
        File file = new File(mFileName);
        file.delete();
    }

    private void startRecording() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chat/record";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        //aac
        path += "/" + System.currentTimeMillis() + ".aac";
        mFileName=path;

        recorder = new MediaRecorder();



        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setAudioChannels(1);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //recorder.setVideoFrameRate(4000);

        recorder.setOutputFile(mFileName);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
        thread = new ObtainDecibelThread();
        thread.start();

    }

    private void stopRecording() {
        if (thread != null) {
            thread.exit();
            thread = null;
        }
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
            }
        } catch (Exception e) {
            LogUtils.e("stop");
        }

    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (recorder == null || !running) {
                    break;
                }
                int x = recorder.getMaxAmplitude();
                if (x != 0) {
                    int f = (int) (10 * Math.log(x) / Math.log(10));
                    if (f < 26)
                        volumeHandler.sendEmptyMessage(1);
                    else if (f < 32)
                        volumeHandler.sendEmptyMessage(2);
                    else if (f < 38)
                        volumeHandler.sendEmptyMessage(3);
                    else if (f < 42)
                        volumeHandler.sendEmptyMessage(4);
                    else
                        volumeHandler.sendEmptyMessage(5);

                }
            }
        }

    }

    private OnDismissListener onDismiss = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    class ShowVolumeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    soundWave.setImageResource(res[msg.what]);
                    break;
                case ONE_MINUTE:
                    volumeHandler.sendEmptyMessageDelayed(ONE_MINUTE, 1000);
                    int counter = (int) (System.currentTimeMillis() - startTime) / 1000;
                    if (counter < 10) {
                        counter_tv.setText("0" + counter + "' / 60'");
                    } else {
                        counter_tv.setText(counter + "' / 60'");
                    }

                    if (System.currentTimeMillis() - startTime > 60000) {
                        volumeHandler.removeMessages(ONE_MINUTE);
                        setText(R.string.recorde_press_str);
                        finishRecord();
                    }
                    break;
            }

        }
    }

    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath, int time);
    }
}
