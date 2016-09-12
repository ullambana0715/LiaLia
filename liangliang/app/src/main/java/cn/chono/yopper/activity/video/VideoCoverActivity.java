package cn.chono.yopper.activity.video;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.duanqu.qupai.bean.QupaiUploadTask;
import com.duanqu.qupai.upload.QupaiUploadListener;
import com.duanqu.qupai.upload.UploadService;
import com.lidroid.xutils.util.LogUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.SubmitVideo.SubmitVideoBean;
import cn.chono.yopper.Service.Http.SubmitVideo.SubmitVideoService;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.utils.video.VideoContant;
import cn.chono.yopper.view.gesture.GestureSlideView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 设置视频封面图
 *
 * @author zxb
 */
public class VideoCoverActivity extends MainFrameActivity implements View.OnClickListener, GestureSlideView.OnValueListener {

    /**
     * 滑动杆
     */
    private GestureSlideView gestureSlideView;
    /**
     * 大图片显示
     */
    private ImageView videoView;
    /**
     * 提交按钮
     */
    private Button submit_but;


    private String _videoPath;

    private String[] _videoImage;
    private String[] _videoImage8;

    private int _index_bitmaps = 0;

    private LinearLayout linearLayout;

    private Dialog _analysisDialog;


    private LinearLayout btnGoBack_container;

    String type;

    protected CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.video_cover_activity);

        initView();
        //接收传递过来的视频地址
        _videoPath = getIntent().getExtras().getString(YpSettings.VIDEO_PATH_NAME);
        _videoImage = getIntent().getExtras().getStringArray(YpSettings.VIDEO_PATH_NAME_IMG);

        type = getIntent().getExtras().getString(YpSettings.VIDEO_PATH_TYPE);

        Logger.e("来源＝" + type);

        if (_analysisDialog == null) {
            _analysisDialog = DialogUtil.LoadingDialog(VideoCoverActivity.this, null);
        }
        _analysisDialog.show();

        _videoImage8 = formerData(_videoImage);
        svaeBitmap();


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置视频封面"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("设置视频封面"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    private void initView() {
        btnGoBack_container = (LinearLayout) findViewById(R.id.btnGoBack_container);

        btnGoBack_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);


        gestureSlideView = (GestureSlideView) findViewById(R.id.gestureSlideView);
        gestureSlideView.setOnValueListener(this);
        videoView = (ImageView) findViewById(R.id.videoView);

        submit_but = (Button) findViewById(R.id.submit_but);
        submit_but.setOnClickListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.width = UnitUtil.getScreenWidthPixels(this);
        params.height = UnitUtil.getScreenWidthPixels(this);

        videoView.setLayoutParams(params);

    }


    private void svaeBitmap() {

        _BitmapListFormer = formerDataBitmap(_videoImage);
        _index_bitmaps = 0;
        isBitmapsExist();

        if (_analysisDialog != null)
            _analysisDialog.dismiss();

        linearLayout.setVisibility(View.VISIBLE);
        getformerDataBitmap(_index_bitmaps);
        gestureSlideView.setData(_videoImage8);
        gestureSlideView.setVideoSeekTo(_Videobitmap);

    }


    private int _bitmapsSize = 0;

    private List<byte[]> _BitmapList = new ArrayList<>();
    private List<Bitmap> _BitmapListFormer = new ArrayList<>();

    /**
     * 判断图片数据是否存在。和获取图片数据的长度
     *
     * @return
     */
    private void isBitmapsExist() {

        if (_BitmapListFormer != null && _BitmapListFormer.size() > 0) {
            _bitmapsSize = _BitmapListFormer.size();
        }

    }

    /**
     * 从趣拍中提取8张
     *
     * @param formerBitmap
     * @return
     */
    private String[] formerData(String[] formerBitmap) {
        String[] videoImage = new String[8];
        for (int i = 0; i < formerBitmap.length; i++) {
            if (i < videoImage.length) {
                videoImage[i] = formerBitmap[i];
            }

        }
        return videoImage;
    }

    private List<Bitmap> formerDataBitmap(String[] formerBitmap) {
        List<Bitmap> bit_list = new ArrayList<>();
        for (int i = 0; i < formerBitmap.length; i++) {
            Bitmap bitmap = ImgUtils.getDiskBitmap(formerBitmap[i]);
            bit_list.add(bitmap);
        }
        return bit_list;
    }

    @Override
    public Bitmap onValueScale(int intervalSize, int currentValue) {


        float interval = (intervalSize) / (_bitmapsSize);
        float currentIndex = (currentValue) / interval;

        int index = (int) currentIndex;

        if (index < 0) {
            _index_bitmaps = 0;
        } else if (index > (_bitmapsSize - 1)) {
            _index_bitmaps = _bitmapsSize - 1;
        } else {
            _index_bitmaps = index;
        }

        System.gc();
        getformerDataBitmap(_index_bitmaps);


        return _Videobitmap;


    }

    @Override
    public void onValueSelected() {
        if (_Videobitmap != null) {

            videoView.setImageBitmap(_Videobitmap);

        }

    }


    private Bitmap _Videobitmap;


    private void getformerDataBitmap(int index_bitmaps) {
        _Videobitmap = _BitmapListFormer.get(index_bitmaps);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        _Videobitmap.recycle();

        for (Bitmap map : _BitmapListFormer) {
            map.recycle();

        }
        _BitmapList.clear();

        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_but:
                ViewsUtils.preventViewMultipleClick(v, false);
                if (_uploadDialog == null) {
                    _uploadDialog = DialogUtil.LoadingDialog(VideoCoverActivity.this, null);
                }
                _uploadDialog.show();


                if (!TextUtils.isEmpty(videoImeHttpUrl) && !TextUtils.isEmpty(videoHttpUrl)) {
                    Message message = new Message();
                    message.what = Http_Uploading;
                    _UploadHandler.sendMessage(message);
                    return;
                }
                videoImeHttpUrl = "";
                videoHttpUrl = "";
                if (TextUtils.isEmpty(_iamgeFilePath)) {//判断图片路径是否存在。当不存在是才去压缩。注意。当上传失败后。需要对“_iamgeFilePath”置空。同时需要生成新的_Videobitmap.避免重复压缩。
                    _iamgeFilePath = ImgUtils.saveVideoImgFile(VideoCoverActivity.this, _Videobitmap);//不能回收Bitmap.因为回收后，当你滑动选择杠时，为因Bitmap的recycled。Canvas：trying to use a recycled bitmap
                }

                onUploadVideo();

                break;
        }

    }

    private Dialog _uploadDialog;
    private Dialog _hintdialog;


    private final int VIDEO_UploadComplete = 2000;
    private final int VIDEO_UploadFailed = 2001;


    private final int Http_UploadComplete = 3000;
    private final int Http_Uploading = 3002;

    private String _iamgeFilePath;

    private String videoImeHttpUrl;
    private String videoHttpUrl;

    Handler _UploadHandler = new Handler() {


        @Override
        public void dispatchMessage(Message msg) {

            super.dispatchMessage(msg);
            int tagId = msg.what;
            switch (tagId) {

                case VIDEO_UploadComplete://视频上传成功

                    Bundle v_bundle = msg.getData();

                    videoHttpUrl = v_bundle.getString("videoHttpUrl");

                    videoImeHttpUrl = v_bundle.getString("ImgHttpUrl");

                    int purposes = SharedprefUtil.getInt(VideoCoverActivity.this, YpSettings.PURPOSE_KEY, 0);

                    if (TextUtils.equals(YpSettings.VIDEO_PATH_TYPE_UserInfoPresenter, type)) {


                        postGeneralVideos(LoginUser.getInstance().getUserId(), videoHttpUrl, videoImeHttpUrl);


                    } else {

                        httpVideoVerification(LoginUser.getInstance().getUserId(), purposes, videoHttpUrl, videoImeHttpUrl);
                    }


                    break;
                case VIDEO_UploadFailed://视频上传失败

                    _hintdialog = DialogUtil.createHintOperateDialog(VideoCoverActivity.this, "提示", "上传失败，请重试", "", "确定", backCallListener);
                    if (!isFinishing()) {
                        _hintdialog.show();
                    }

                    if (_uploadDialog != null) {
                        if (!isFinishing()) {
                            _uploadDialog.dismiss();
                        }
                    }
                    break;
                case Http_Uploading://上传自己服务器开始


                    if (TextUtils.equals(YpSettings.VIDEO_PATH_TYPE_UserInfoPresenter, type)) {


                        postGeneralVideos(LoginUser.getInstance().getUserId(), videoHttpUrl, videoImeHttpUrl);


                    } else {

                        httpVideoVerification(LoginUser.getInstance().getUserId(), 1, videoHttpUrl, videoImeHttpUrl);
                    }


                    break;
                case Http_UploadComplete://上传自己服务器成功

                    DialogUtil.showDisCoverNetToast(VideoCoverActivity.this, "上传成功");

                    if (_uploadDialog != null) {
                        if (!isFinishing()) {
                            _uploadDialog.dismiss();
                        }
                    }
                    ViewsUtils.preventViewMultipleClick(submit_but, true);

                    Bundle bundle = new Bundle();


                    bundle.putString(YpSettings.VIDEO_PATH_TYPE, type);

                    ActivityUtil.jump(VideoCoverActivity.this, VideoCommitFinishActivity.class, bundle, 0, 100);

                    VideoCoverActivity.this.finish();


                    break;

            }
        }
    };


    private BackCallListener backCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (_hintdialog != null)
                _hintdialog.dismiss();
            ViewsUtils.preventViewMultipleClick(submit_but, true);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (_hintdialog != null)
                _hintdialog.dismiss();
            ViewsUtils.preventViewMultipleClick(submit_but, true);

        }
    };


    private void onUploadVideo() {

        UploadService uploadService = UploadService.getInstance();

        uploadService.setQupaiUploadListener(
                new QupaiUploadListener() {
                    @Override
                    public void onUploadProgress(String uuid, long uploadedBytes, long totalBytes) {

                    }

                    @Override
                    public void onUploadError(String uuid, int errorCode, String message) {

                        Message msg = new Message();
                        msg.what = VIDEO_UploadFailed;
                        _UploadHandler.sendMessage(msg);
                    }

                    @Override
                    public void onUploadComplte(String uuid, int responseCode, String responseMessage) {

                        String videoUrl = VideoContant.domain + "/v/" + responseMessage + ".mp4" + "?token =" + VideoContant.accessToken;

                        String imageUrl = VideoContant.domain + "/v/" + responseMessage + ".jpg" + "?token=" + VideoContant.accessToken;

                        Message message = new Message();
                        message.what = VIDEO_UploadComplete;
                        Bundle bundle = new Bundle();
                        bundle.putString("videoHttpUrl", videoUrl);
                        bundle.putString("ImgHttpUrl", imageUrl);

                        message.setData(bundle);
                        _UploadHandler.sendMessage(message);

                    }
                }

        );

        String uuid = UUID.randomUUID().toString();

        QupaiUploadTask task = uploadService.createTask(this, uuid, new File(_videoPath), new File(_iamgeFilePath), VideoContant.accessToken, VideoContant.space, VideoContant.shareType, VideoContant.tags, VideoContant.description);

        startUpload(task);

    }


    /**
     * 开始上传
     *
     * @param data 上传任务的task
     */
    private void startUpload(QupaiUploadTask data) {
        try {
            UploadService uploadService = UploadService.getInstance();
            uploadService.startUpload(data);
        } catch (IllegalArgumentException exc) {

        }
    }

    private void httpVideoVerification(int userId, int purpose, String videoUrl, String coverImgUrl) {


        SubmitVideoBean videoBean = new SubmitVideoBean();
        videoBean.setUserId(userId);
        videoBean.setCoverImgUrl(coverImgUrl);
        videoBean.setVideoUrl(videoUrl);
        videoBean.setPurpose(purpose);


        SubmitVideoService videoService = new SubmitVideoService(this);
        videoService.parameter(videoBean);
        videoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                Message message = new Message();
                message.what = Http_UploadComplete;
                _UploadHandler.sendMessage(message);
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                ViewsUtils.preventViewMultipleClick(submit_but, true);
                if (_uploadDialog != null) {
                    _uploadDialog.dismiss();
                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(VideoCoverActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(VideoCoverActivity.this, msg);
            }
        });
        videoService.enqueue();


    }


    private void postGeneralVideos(int userId, String videoUrl, String coverImgUrl) {


        Subscription subscription = HttpFactory.getHttpApi().postGeneralVideos(userId, videoUrl, coverImgUrl)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(b -> {

                    Message message = new Message();
                    message.what = Http_UploadComplete;
                    _UploadHandler.sendMessage(message);


                }, throwable -> {

                    ViewsUtils.preventViewMultipleClick(submit_but, true);

                    if (_uploadDialog != null) {

                        _uploadDialog.dismiss();
                    }

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null || TextUtils.isEmpty(apiResp.getMsg())) {


                        DialogUtil.showDisCoverNetToast(VideoCoverActivity.this);

                    } else {


                        DialogUtil.showDisCoverNetToast(VideoCoverActivity.this, apiResp.getMsg());

                    }


                });


        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);


    }


}
