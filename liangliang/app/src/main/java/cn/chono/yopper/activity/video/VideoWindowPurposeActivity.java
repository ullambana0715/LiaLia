package cn.chono.yopper.activity.video;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.SharedprefUtil;

/**
 * 选择交友目的
 *
 * @author sam.sun
 */
public class VideoWindowPurposeActivity extends MainFrameActivity implements OnClickListener {


    private ImageView video_window_purpose_friend;

    private ImageView video_window_purpose_love;

    private ImageView video_window_purpose_married;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_purpose_window_layout);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//需要添加

        initComponent();
    }


    /**
     * 初始化
     */
    private void initComponent() {

        video_window_purpose_friend = (ImageView) this.findViewById(R.id.video_window_purpose_friend);
        video_window_purpose_friend.setOnClickListener(this);

        video_window_purpose_love = (ImageView) this.findViewById(R.id.video_window_purpose_love);
        video_window_purpose_love.setOnClickListener(this);

        video_window_purpose_married = (ImageView) this.findViewById(R.id.video_window_purpose_married);
        video_window_purpose_married.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.video_window_purpose_friend:
                SharedprefUtil.saveInt(VideoWindowPurposeActivity.this, YpSettings.PURPOSE_KEY, 1);
                finish();
                break;

            case R.id.video_window_purpose_love:
                SharedprefUtil.saveInt(VideoWindowPurposeActivity.this, YpSettings.PURPOSE_KEY, 2);
                finish();
                break;

            case R.id.video_window_purpose_married:
                SharedprefUtil.saveInt(VideoWindowPurposeActivity.this, YpSettings.PURPOSE_KEY, 3);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return true;
    }
}
