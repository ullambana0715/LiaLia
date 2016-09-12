package cn.chono.yopper.activity.video;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.duanqu.qupai.utils.AppGlobalSetting;
import com.google.common.io.Files;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.IOException;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileBean;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.SubmitVideoOpen.SubmitVideoOpenBean;
import cn.chono.yopper.Service.Http.SubmitVideoOpen.SubmitVideoOpenRespBean;
import cn.chono.yopper.Service.Http.SubmitVideoOpen.SubmitVideoOpenService;
import cn.chono.yopper.Service.Http.SubmitVideoUserOnly.SubmitVideoUserOnlyBean;
import cn.chono.yopper.Service.Http.SubmitVideoUserOnly.SubmitVideoUserOnlyRespBean;
import cn.chono.yopper.Service.Http.SubmitVideoUserOnly.SubmitVideoUserOnlyService;
import cn.chono.yopper.Service.OKHttpListener;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.event.VideoLookLimitChangeEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SHA;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.video.VideoContant;
import cn.chono.yopper.utils.video.VideoFileUtils;
import cn.chono.yopper.utils.video.VideoRecordResult;
import cn.chono.yopper.utils.video.VideoRequestCode;
import cn.chono.yopper.view.SwitchButton;

/**
 * 我的视频详情页面
 *
 * @author sam.sun
 */
public class VideoDetailActivity extends MainFrameActivity implements OnClickListener {

    private RelativeLayout video_detail_video_layout;
    private SurfaceView video_detail_videoview;
    private ImageView video_detail_coverimg_iv;
    private ImageView video_detail_video_start_iv;

    private LinearLayout video_detail_myvedio_layout;


    private SwitchButton video_detail_public_vedio_switch;

    private RelativeLayout video_detail_refuse_receive_msg_layout;
    private SwitchButton video_detail_refuse_receive_msg;

    private RelativeLayout video_detail_makeFriend_layout;
    private TextView video_detail_makeFriend_tv;

    private TextView video_detail_certification_standard_tv;

    private LinearLayout video_detail_hint_layout;

    private TextView video_detail_hint_tv;

    private ProgressBar video_detail_video_loading_pb;

    private int userid;
    private boolean isopen;
    private boolean ischatwithVideouserOnly;

    //0,未认证或者未通过；1,审核中; 2，已认证且通过；
    private int vediostatus;
    private String videoUrl;
    private String coverImgUrl;

    //1交朋友；2找恋人；3找结婚对象
    private int purpose;
    //1为男性
    private int sex;
    private Dialog hintdialog;

    private Dialog loadingDiaog;
    private boolean isPost = false;

    private int mDisplay_width;

    private MediaPlayer mp;     //声明MediaPlayer对象

    private boolean isfirst = true;

    private boolean isgetVideo = false;

    private String frompage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        Display mDisplay = this.getWindowManager().getDefaultDisplay();
        mDisplay_width = mDisplay.getWidth();

        setContentLayout(R.layout.video_detail_activity);
        userid = LoginUser.getInstance().getUserId();

        mp = new MediaPlayer();       //实例化MediaPlayer对象

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            vediostatus = bundle.getInt(YpSettings.VIDEO_STATE);
            if (bundle.containsKey(YpSettings.VIDEO_URL)) {
                videoUrl = bundle.getString(YpSettings.VIDEO_URL);
            }
            if (bundle.containsKey(YpSettings.VIDEO_IMG_URl)) {
                coverImgUrl = bundle.getString(YpSettings.VIDEO_IMG_URl);
            }
            if (bundle.containsKey(YpSettings.VIDEO_PURPOSE)) {
                purpose = bundle.getInt(YpSettings.VIDEO_PURPOSE);
            }
            if (bundle.containsKey(YpSettings.VIDEO_OPEN)) {
                isopen = bundle.getBoolean(YpSettings.VIDEO_OPEN);
            }
            if (bundle.containsKey(YpSettings.VIDEO_CHAT_WITH_USER_ONLY)) {
                ischatwithVideouserOnly = bundle.getBoolean(YpSettings.VIDEO_CHAT_WITH_USER_ONLY);
            }

            if(bundle.containsKey(YpSettings.FROM_PAGE)){
                frompage=bundle.getString(YpSettings.FROM_PAGE);
            }

        }
        sex = DbHelperUtils.getDbUserSex(userid);
        initComponent();


    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("视频详情页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
        setDateToView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("视频详情页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
        if (mp.isPlaying()) {
            video_detail_coverimg_iv.setVisibility(View.GONE);
            video_detail_video_start_iv.setVisibility(View.VISIBLE);
            video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
            mp.pause();
        }

        mp.reset();
    }

    /**
     * 初始化
     */
    private void initComponent() {

        this.getTvTitle().setText("认证视频");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.VISIBLE);
        this.getBtnOption().setVisibility(View.GONE);
        this.gettvOption().setVisibility(View.VISIBLE);
        this.gettvOption().setText("更换视频");
        this.getGoBackLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.getOptionLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedprefUtil.saveInt(VideoDetailActivity.this, YpSettings.PURPOSE_KEY, purpose);
                //更换视频 直接跳转到拍摄页面 不用弹目的窗口
                if (null != App.getInstance().qupaiService) {

                    //引导，只显示一次，这里用SharedPreferences记录
                    final AppGlobalSetting sp = new AppGlobalSetting(getApplicationContext());
                    Boolean isGuideShow = sp.getBooleanGlobalItem(YpSettings.PREF_VIDEO_EXIST_USER, true);

                    /**
                     * 建议上面的initRecord只在application里面调用一次。这里为了能够开发者直观看到改变所以可以调用多次
                     */
                    App.getInstance().qupaiService.showRecordPage(VideoDetailActivity.this, YpSettings.RECORDE_SHOW, isGuideShow);

                    sp.saveGlobalConfigItem(YpSettings.PREF_VIDEO_EXIST_USER, false);

                }
            }
        });

        video_detail_video_layout = (RelativeLayout) this.findViewById(R.id.video_detail_video_layout);
        try {
            video_detail_video_layout.setDrawingCacheEnabled(false);
        } catch (Exception e) {
            Bitmap bitmap = video_detail_video_layout.getDrawingCache().copy(Bitmap.Config.RGB_565, true);
            video_detail_video_layout.setDrawingCacheEnabled(false);
        }


        video_detail_videoview = (SurfaceView) this.findViewById(R.id.video_detail_videoview);
        video_detail_coverimg_iv = (ImageView) this.findViewById(R.id.video_detail_coverimg_iv);
        video_detail_video_start_iv = (ImageView) this.findViewById(R.id.video_detail_video_start_iv);
        video_detail_video_loading_pb = (ProgressBar) this.findViewById(R.id.video_detail_video_loading_pb);


        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) video_detail_video_layout.getLayoutParams();
        linearParams.height = mDisplay_width;
        linearParams.width = mDisplay_width;
        video_detail_video_layout.setLayoutParams(linearParams);


        video_detail_myvedio_layout = (LinearLayout) this.findViewById(R.id.video_detail_myvedio_layout);


        video_detail_public_vedio_switch = (SwitchButton) this.findViewById(R.id.video_detail_public_vedio_switch);

        video_detail_refuse_receive_msg_layout = (RelativeLayout) this.findViewById(R.id.video_detail_refuse_receive_msg_layout);
        video_detail_refuse_receive_msg = (SwitchButton) this.findViewById(R.id.video_detail_refuse_receive_msg);

        video_detail_makeFriend_layout = (RelativeLayout) this.findViewById(R.id.video_detail_makeFriend_layout);
        video_detail_makeFriend_layout.setOnClickListener(this);
        video_detail_makeFriend_tv = (TextView) this.findViewById(R.id.video_detail_makeFriend_tv);

        video_detail_certification_standard_tv = (TextView) this.findViewById(R.id.video_detail_certification_standard_tv);
        video_detail_certification_standard_tv.setOnClickListener(this);

        video_detail_hint_layout = (LinearLayout) this.findViewById(R.id.video_detail_hint_layout);

        video_detail_hint_tv = (TextView) this.findViewById(R.id.video_detail_hint_tv);

        video_detail_public_vedio_switch.setChecked(isopen);
        video_detail_public_vedio_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //是否公开视频
                if (!isPost) {
                    isPost = true;
                    loadingDiaog = DialogUtil.LoadingDialog(VideoDetailActivity.this, null);
                    if (!isFinishing()) {
                        loadingDiaog.show();
                    }
                    submitVideoOpen(isChecked);

                }
            }
        });



        video_detail_refuse_receive_msg.setChecked(ischatwithVideouserOnly);
        video_detail_refuse_receive_msg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //是否接收未公开视频消息
                if (!isPost) {
                    isPost = true;
                    loadingDiaog = DialogUtil.LoadingDialog(VideoDetailActivity.this, null);
                    if (!isFinishing()) {
                        loadingDiaog.show();
                    }
                    chatWithVideoUserOnly(isChecked);
                }
            }
        });



        video_detail_video_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String localpath = getvideoExistsLocalPath();
                if (!CheckUtil.isEmpty(localpath)) {
                    playDeal();
                } else {
                    if (isgetVideo) {
                        return;
                    }
                    video_detail_video_start_iv.setVisibility(View.GONE);
                    video_detail_video_loading_pb.setVisibility(View.VISIBLE);
                    isgetVideo = true;
                    getMusicFile(videoUrl);


                }


            }
        });

        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                video_detail_coverimg_iv.setVisibility(View.VISIBLE);
                video_detail_video_start_iv.setVisibility(View.VISIBLE);
                video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);

                hintdialog = DialogUtil.createHintOperateDialog(VideoDetailActivity.this, "", "认证视频存在异常，暂时无法查看", "", "确认", backCallListener);
                if (!isFinishing()) {
                    hintdialog.show();
                }
                return true;
            }
        });
        //为MediaPlayer对象添加完成事件监听器
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                video_detail_video_loading_pb.setVisibility(View.GONE);
                video_detail_coverimg_iv.setVisibility(View.VISIBLE);
                video_detail_video_start_iv.setVisibility(View.VISIBLE);
                video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
               mp.reset();

            }

        });

    }

    private void playDeal() {
        boolean isPlaying = false;
        try {
            isPlaying = mp.isPlaying();
        } catch (IllegalStateException e) {

        }

        if (isPlaying) {
            video_detail_video_loading_pb.setVisibility(View.GONE);
            video_detail_coverimg_iv.setVisibility(View.GONE);
            video_detail_video_start_iv.setVisibility(View.VISIBLE);
            video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
            mp.pause();
        } else {


            video_detail_video_start_iv.setVisibility(View.GONE);
            video_detail_video_loading_pb.setVisibility(View.GONE);
            setvideoData();

        }
    }

    private String getvideoExistsLocalPath() {
        final String filename = SHA.encodeByMD5(videoUrl);
        if (VideoFileUtils.isMyVideoFileExists(this, filename)) {
            File file = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory() + File.separator + "video" + File.separator + "myvideo" + File.separator + filename + ".mp4");
            } else {
                file = new File(VideoDetailActivity.this.getFilesDir() + File.separator + "video" + File.separator + "myvideo" + File.separator + filename + ".mp4");
            }
            if (file.exists()) {
                return file.getAbsolutePath();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void setvideoData() {
        String localpath = getvideoExistsLocalPath();
        if (!CheckUtil.isEmpty(localpath)) {
            try {
                video_detail_coverimg_iv.setVisibility(View.GONE);
                mp.setDataSource(localpath);
                mp.setDisplay(video_detail_videoview.getHolder());  //设置将视频画面输出到SurfaceView
                mp.prepare(); //预加载视频
                mp.start(); //开始播放


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给view设置数据
     */

    private void setDateToView() {

        if (vediostatus == 1) {
            //审核中
            video_detail_myvedio_layout.setVisibility(View.GONE);
            video_detail_hint_layout.setVisibility(View.VISIBLE);
            video_detail_hint_tv.setText("您的视频正在审核中，请耐心等待...");

            if (CheckUtil.isEmpty(videoUrl)) {
                hintdialog = DialogUtil.createHintOperateDialog(VideoDetailActivity.this, "", "认证视频存在异常，暂时无法查看", "", "确认", backCallListener);
                if (!isFinishing()) {
                    hintdialog.show();
                }
            }

            //设置封面
            if (!CheckUtil.isEmpty(coverImgUrl)) {
                Glide.with(this).load(coverImgUrl).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        video_detail_video_start_iv.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        video_detail_video_start_iv.setVisibility(View.VISIBLE);
                        video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
                        return false;
                    }
                }).into(video_detail_coverimg_iv);
            } else {
                video_detail_coverimg_iv.setVisibility(View.GONE);
            }


            //
        } else if (vediostatus == 2) {
            //审核通过
            video_detail_myvedio_layout.setVisibility(View.VISIBLE);
            video_detail_hint_layout.setVisibility(View.GONE);

            if (CheckUtil.isEmpty(videoUrl)) {
                LogUtils.e("视频地址空的吗");
                hintdialog = DialogUtil.createHintOperateDialog(VideoDetailActivity.this, "", "认证视频存在异常，暂时无法查看", "", "确认", backCallListener);
                if (!isFinishing()) {
                    hintdialog.show();
                }
            }

            if (sex == 2) {
                video_detail_refuse_receive_msg_layout.setVisibility(View.VISIBLE);
            } else {
                video_detail_refuse_receive_msg_layout.setVisibility(View.GONE);
            }

            if (purpose == 1) {
                video_detail_makeFriend_tv.setText("结交知己好友");
            } else if (purpose == 2) {
                video_detail_makeFriend_tv.setText("邂逅浪漫爱情");
            } else {
                video_detail_makeFriend_tv.setText("寻找结婚对象");
            }

            //设置封面
            if (!CheckUtil.isEmpty(coverImgUrl)) {
                Glide.with(this).load(coverImgUrl).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        video_detail_video_start_iv.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        video_detail_video_start_iv.setVisibility(View.VISIBLE);
                        video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
                        return false;
                    }
                }).into(video_detail_coverimg_iv);
            } else {
                video_detail_coverimg_iv.setVisibility(View.GONE);
            }


        } else {
            LogUtils.e("视频类型错误吗");
            hintdialog = DialogUtil.createHintOperateDialog(VideoDetailActivity.this, "", "认证视频存在异常，暂时无法查看", "", "确认", backCallListener);
            if (!isFinishing()) {
                hintdialog.show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.video_detail_makeFriend_layout://修改交友目的
                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.VIDEO_PURPOSE, purpose);
                bundle.putInt(YpSettings.INTENT_RESULT_CODE, 1000);
                ActivityUtil.jumpForResult(VideoDetailActivity.this, ChangeDatingPurposeActivity.class, bundle, 1000, 0, 100);
                break;

            case R.id.video_detail_certification_standard_tv://视频认证规范
                Bundle bundles = new Bundle();
                bundles.putString(YpSettings.BUNDLE_KEY_WEB_URL, "video");
                bundles.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "视频认证规范");
                bundles.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

                ActivityUtil.jump(VideoDetailActivity.this, SimpleWebViewActivity.class, bundles, 0, 100);
                break;
        }
    }

    private BackCallListener backCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
                finish();
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null) {
            Bundle bundle = data.getExtras();
            if (resultCode == 1000) {
                if (bundle != null) {
                    purpose = bundle.getInt(YpSettings.VIDEO_PURPOSE);
                    if (purpose == 1) {
                        video_detail_makeFriend_tv.setText("结交知己好友");
                    } else if (purpose == 2) {
                        video_detail_makeFriend_tv.setText("邂逅浪漫爱情");
                    } else {
                        video_detail_makeFriend_tv.setText("寻找结婚对象");
                    }
                }
                return;
            }
            if (requestCode == VideoRequestCode.RECORDE_SHOW) {
                if (resultCode == Activity.RESULT_OK) {

                    VideoRecordResult vrr = new VideoRecordResult(data);
                    String videoPath = vrr.getPath();
                    String[] image = vrr.getThumbnail();


                    try {


                        //拷贝move操作
                        Files.move(new File(videoPath), new File(VideoContant.VIDEOPATH));
                        String[] images = new String[10];
                        for (int i = 0; i < image.length; i++) {
                            String imageUrl = VideoContant.THUMBPATH + System.currentTimeMillis() + ".png";
                            Files.move(new File(image[i]), new File(imageUrl));
                            images[i] = imageUrl;
                        }


                        //清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作
                        if (null != App.getInstance().qupaiService) {
                            App.getInstance().qupaiService.deleteDraft(VideoDetailActivity.this, data);

                        }

                        //跳转到编辑界面
                        Bundle be = new Bundle();
                        be.putString(YpSettings.VIDEO_PATH_NAME, VideoFileUtils.newOutgoingFilePath());
                        be.putStringArray(YpSettings.VIDEO_PATH_NAME_IMG, images);
                        ActivityUtil.jump(VideoDetailActivity.this, VideoCoverActivity.class, be, 0, 100);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    //是否公开视频
    private void submitVideoOpen(final boolean open) {


        SubmitVideoOpenBean openBean = new SubmitVideoOpenBean();
        openBean.setUserId(LoginUser.getInstance().getUserId());

        openBean.setOpen(open);

        SubmitVideoOpenService openService = new SubmitVideoOpenService(this);
        openService.parameter(openBean);
        openService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                SubmitVideoOpenRespBean openRespBean = (SubmitVideoOpenRespBean) respBean;
                SubmitVideoOpenRespBean.OpenResp openResp = openRespBean.getResp();

                loadingDiaog.dismiss();
                isPost = false;
                if (null != openResp) {
                    isopen = openResp.isOpen();
                    video_detail_public_vedio_switch.setChecked(isopen);
                }

                if(!CheckUtil.isEmpty(frompage) && frompage.equals("OthersVideoDetailActivity")){

                    RxBus.get().post("VideoLookLimitChangeEvent",new VideoLookLimitChangeEvent());
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                loadingDiaog.dismiss();
                isPost = false;
                video_detail_public_vedio_switch.setChecked(isopen);

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(VideoDetailActivity.this);
                    return;

                }
                DialogUtil.showDisCoverNetToast(VideoDetailActivity.this, msg);

            }
        });
        openService.enqueue();


    }

    //是否拒绝非视频认证用户消息
    private void chatWithVideoUserOnly(boolean isrefuse) {


        SubmitVideoUserOnlyBean userOnlyBean = new SubmitVideoUserOnlyBean();
        userOnlyBean.setUserId(LoginUser.getInstance().getUserId());

        userOnlyBean.setChatWithVideoUserOnly(isrefuse);

        SubmitVideoUserOnlyService onlyService = new SubmitVideoUserOnlyService(this);
        onlyService.parameter(userOnlyBean);
        onlyService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                SubmitVideoUserOnlyRespBean onlyRespBean = (SubmitVideoUserOnlyRespBean) respBean;
                SubmitVideoUserOnlyRespBean.VideouserOnly videouserOnly = onlyRespBean.getResp();

                loadingDiaog.dismiss();
                isPost = false;
                if (null != videouserOnly) {
                    ischatwithVideouserOnly = videouserOnly.isChatWithVideoUserOnly();
                    video_detail_refuse_receive_msg.setChecked(ischatwithVideouserOnly);

                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();
                isPost = false;
                video_detail_refuse_receive_msg.setChecked(ischatwithVideouserOnly);
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(VideoDetailActivity.this, msg);
                    return;

                }
                DialogUtil.showDisCoverNetToast(VideoDetailActivity.this, msg);
            }
        });
        onlyService.enqueue();


    }


    private void getMusicFile(final String url) {
        final String filename = SHA.encodeByMD5(url);


        DownloadMusicFileBean musicFileBean = new DownloadMusicFileBean();
        musicFileBean.setUrl(url);

        DownloadMusicFileService fileService = new DownloadMusicFileService(this);
        fileService.setOKHttpListener(new OKHttpListener() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);

                byte[] bytes = (byte[]) result;

                if (bytes != null && bytes.length > 0) {
                    if (!VideoFileUtils.saveMyVideoFile(VideoDetailActivity.this, bytes, filename)) {
                        isgetVideo = false;
                        DialogUtil.showDisCoverNetToast(VideoDetailActivity.this, "未知错误");
                        video_detail_video_loading_pb.setVisibility(View.GONE);
                        video_detail_coverimg_iv.setVisibility(View.VISIBLE);
                        video_detail_video_start_iv.setVisibility(View.VISIBLE);
                    } else {
                        isgetVideo = false;
                        playDeal();
                    }


                }

            }

            @Override
            public void onFail(Object result) {
                super.onFail(result);
                isgetVideo = false;
            }
        });
        fileService.parameter(musicFileBean);

        fileService.enqueue();

//
    }

    @Override
    protected void onDestroy() {
        if (mp.isPlaying()) {
            mp.stop();  //停止播放视频
        }
        mp.release();   //释放资源
        super.onDestroy();
    }

}