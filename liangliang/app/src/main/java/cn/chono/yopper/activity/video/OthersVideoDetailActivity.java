package cn.chono.yopper.activity.video;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.util.LogUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.IOException;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileBean;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileService;
import cn.chono.yopper.Service.Http.InviteType.InviteTypeBean;
import cn.chono.yopper.Service.Http.InviteType.InviteTypeRespBean;
import cn.chono.yopper.Service.Http.InviteType.InviteTypeService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.VedioDetail.VedioDetailBean;
import cn.chono.yopper.Service.Http.VedioDetail.VedioDetailRespBean;
import cn.chono.yopper.Service.Http.VedioDetail.VedioDetailService;
import cn.chono.yopper.Service.OKHttpListener;
import cn.chono.yopper.activity.usercenter.VipOpenedActivity;
import cn.chono.yopper.activity.usercenter.VipRenewalsActivity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.VideoDetailDto;
import cn.chono.yopper.entity.AttamptRespDto;
import cn.chono.yopper.event.VideoLookLimitChangeEvent;
import cn.chono.yopper.glide.BlurTransformation;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SHA;
import cn.chono.yopper.utils.video.VideoFileUtils;

/**
 * 他人视频详情页面
 *
 * @author sam.sun
 */
public class OthersVideoDetailActivity extends MainFrameActivity {

    private LayoutInflater mInflater;
    private View contextView;
    private RelativeLayout others_video_detail_video_layout;
    private SurfaceView others_video_detail_videoview;
    private ImageView others_video_detail_coverimg_iv;
    private ImageView others_video_detail_video_start_iv;
    private ProgressBar others_video_detail_video_loading_pb;

    private LinearLayout others_video_detail_hint_layout;
    private TextView others_video_detail_hint_tv;

    //c1=3,都通过并公开-正常查看；c2=4,对方未公开-无法查看; c3=5,查看者未通过视频认证-无法查看-模糊处理；c4=6,都通过，查看者未公开;c5_1=7,视频异常 确定返回；c5_2=8,访问者没有视频；UserVideo_Exceed = 9
    private int vediostatus;

    private String videoUrl;
    private String videoImgUrl;

    private BlurTransformation blurtransformation;

    private BitmapPool mPool;

    private Dialog hintdialog;
    private Dialog loadingDiaog, invite_p_dialog;

    private boolean isPostinvite = false;
    private int userid;

    private int mDisplay_width;

    private MediaPlayer mp;     //声明MediaPlayer对象


    private boolean isgetVideo = false;

    /**
     * surfaceView播放控制
     */
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        RxBus.get().register(this);

        Display mDisplay = this.getWindowManager().getDefaultDisplay();
        mDisplay_width = mDisplay.getWidth();
        mp = new MediaPlayer();       //实例化MediaPlayer对象

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            userid = bundle.getInt(YpSettings.USERID);
            vediostatus = bundle.getInt(YpSettings.VIDEO_STATE);
            videoImgUrl = bundle.getString(YpSettings.VIDEO_IMG_URl);
            videoUrl = bundle.getString(YpSettings.VIDEO_URL);
        }
        mPool = Glide.get(this).getBitmapPool();
        blurtransformation = new BlurTransformation(this, mPool, 15, 15);
        initComponent();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("他人视频详情页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
        setDateToView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("他人视频详情页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

        if (mp.isPlaying()) {
            others_video_detail_coverimg_iv.setVisibility(View.GONE);
            others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
            others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
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
        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getGoBackLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.others_video_detail_activity, null);
        others_video_detail_video_layout = (RelativeLayout) contextView.findViewById(R.id.others_video_detail_video_layout);

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) others_video_detail_video_layout.getLayoutParams();
        linearParams.height = mDisplay_width;
        linearParams.width = mDisplay_width;
        others_video_detail_video_layout.setLayoutParams(linearParams);

        others_video_detail_videoview = (SurfaceView) contextView.findViewById(R.id.others_video_detail_videoview);
        others_video_detail_coverimg_iv = (ImageView) contextView.findViewById(R.id.others_video_detail_coverimg_iv);
        others_video_detail_video_start_iv = (ImageView) contextView.findViewById(R.id.others_video_detail_video_start_iv);
        others_video_detail_hint_layout = (LinearLayout) contextView.findViewById(R.id.others_video_detail_hint_layout);
        others_video_detail_video_loading_pb = (ProgressBar) contextView.findViewById(R.id.others_video_detail_video_loading_pb);
        others_video_detail_hint_tv = (TextView) contextView.findViewById(R.id.others_video_detail_hint_tv);

        surfaceHolder = others_video_detail_videoview.getHolder();
        // 设置Holder类型,该类型表示surfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
            others_video_detail_video_layout.setDrawingCacheEnabled(false);
        } catch (Exception e) {
            Bitmap bitmap = others_video_detail_video_layout.getDrawingCache().copy(Bitmap.Config.RGB_565, true);
            others_video_detail_video_layout.setDrawingCacheEnabled(false);
        }

        others_video_detail_video_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (vediostatus) {
                    case 3://可以播放
                        String localpath = getvideoExistsLocalPath();
                        if (!CheckUtil.isEmpty(localpath)) {
                            playDeal();
                        } else {
                            if (isgetVideo) {
                                return;
                            }
                            others_video_detail_video_start_iv.setVisibility(View.GONE);
                            others_video_detail_video_loading_pb.setVisibility(View.VISIBLE);
                            isgetVideo = true;
                            getMusicFile(videoUrl);

                            return;
                        }
                        break;

                    case 4:
                        hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "对方未公开认证视频，是否邀请公开？", "取消", "邀请", InviteOpenCallListener);
                        if (!isFinishing()) {
                            hintdialog.show();
                        }
                        break;

                    case 5:
                        hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "你尚未通过视频认证，无法查看，是否现在认证？", "取消", "现在就去", OpenAndVerificationgCallListener);
                        if (!isFinishing()) {
                            hintdialog.show();
                        }
                        break;

                    case 6:
                        hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "你未公开认证视频，无法查看，是否需要公开？", "取消", "立即公开", OpenAndVerificationgCallListener);
                        if (!isFinishing()) {
                            hintdialog.show();
                        }
                        break;

                    case 9:
                        hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "普通用户每天只能免费看5个视频，VIP不受此限制", "取消", "查看VIP", VIPCallListener);
                        if (!isFinishing()) {
                            hintdialog.show();
                        }
                        break;

                }
            }
        });


        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                LogUtils.e("错误类型what=" + what);
                LogUtils.e("错误类型extra=" + extra);

                others_video_detail_video_layout.setVisibility(View.GONE);
                others_video_detail_hint_layout.setVisibility(View.GONE);
                hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "对方认证视频存在异常，暂时无法查看", "", "确认", gobackCallListener);
                if (!isFinishing()) {
                    hintdialog.show();
                }
                return true;
            }
        });
        //为MediaPlayer对象添加完成事件监听器
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                others_video_detail_coverimg_iv.setVisibility(View.VISIBLE);
                others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
                others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);

                mp.reset();

            }
        });


        this.getMainLayout().addView(contextView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }


    private void playDeal() {
        if (mp.isPlaying()) {
            others_video_detail_video_loading_pb.setVisibility(View.GONE);
            others_video_detail_coverimg_iv.setVisibility(View.GONE);
            others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
            others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
            mp.pause();
        } else {


            try {

                others_video_detail_video_loading_pb.setVisibility(View.GONE);

                others_video_detail_video_start_iv.setVisibility(View.GONE);

                setvideoData();

            } catch (Exception e) {
                Logger.e("playDeal=" + e.getMessage());
                e.printStackTrace();
            }

        }


    }


    private void setvideoData() {
        String localpath = getvideoExistsLocalPath();
        if (!CheckUtil.isEmpty(localpath)) {
            try {

                others_video_detail_coverimg_iv.setVisibility(View.GONE);
                mp.setDataSource(localpath);
                mp.setDisplay(others_video_detail_videoview.getHolder());  //设置将视频画面输出到SurfaceView
                mp.prepare(); //预加载视频
                mp.start(); //开始播放


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Logger.e(localpath);
    }

    private String getvideoExistsLocalPath() {
        final String filename = SHA.encodeByMD5(videoUrl);
        if (VideoFileUtils.isMyVideoFileExists(this, filename)) {
            File file = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory() + File.separator + "video" + File.separator + "myvideo" + File.separator + filename + ".mp4");
            } else {
                file = new File(OthersVideoDetailActivity.this.getFilesDir() + File.separator + "video" + File.separator + "myvideo" + File.separator + filename + ".mp4");
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

    private void DeletevideoExistsLocal() {
        final String filename = SHA.encodeByMD5(videoUrl);
        if (VideoFileUtils.isMyVideoFileExists(this, filename)) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "video" + File.separator + "myvideo" + File.separator + filename + ".mp4");
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 给view设置数据
     */

    private void setDateToView() {

        switch (vediostatus) {
            case 3:
                others_video_detail_video_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_tv.setText("用户通过手机录制并上传一段简短的自拍视频，工作人员会在24小时之内对其进行审核。我们会努力保障用户的真实性。");
                //封面不模糊，视频不上锁


                if (!CheckUtil.isEmpty(videoImgUrl)) {
                    Glide.with(this).load(videoImgUrl).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            others_video_detail_video_start_iv.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
                            others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
                            return false;
                        }
                    }).into(others_video_detail_coverimg_iv);
                } else {
                    others_video_detail_coverimg_iv.setVisibility(View.GONE);
                }

                if (CheckUtil.isEmpty(videoUrl)) {
                    LogUtils.e("视频地址空");
                    others_video_detail_video_layout.setVisibility(View.GONE);
                    others_video_detail_hint_layout.setVisibility(View.GONE);
                    hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "对方认证视频存在异常，暂时无法查看", "", "确认", gobackCallListener);
                    if (!isFinishing()) {
                        hintdialog.show();
                    }
                }
                break;
            case 4:
                others_video_detail_video_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_tv.setText("对方未公开认证视频，无法查看");
                //视频上锁
                others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
                others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_no_play_icon);
                others_video_detail_coverimg_iv.setBackgroundResource(R.drawable.video_no_look_bg);

                break;
            case 5:
                others_video_detail_video_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_tv.setText("你还未通过视频认证，无法查看该用户认证视频");
                //封面高斯模糊，视频上锁
                others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
                others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_no_play_icon);

                if (!CheckUtil.isEmpty(videoImgUrl)) {
                    Glide.with(this).load(videoImgUrl).bitmapTransform(blurtransformation).into(others_video_detail_coverimg_iv);
                } else {
                    others_video_detail_coverimg_iv.setVisibility(View.GONE);
                }
                break;
            case 6:
                others_video_detail_video_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_tv.setText("你未公开认证视频，无法查看该用户认证视频");
                //视频上锁
                others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
                others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_no_play_icon);
                others_video_detail_coverimg_iv.setBackgroundResource(R.drawable.video_no_look_bg);


                break;
            case 7:
                others_video_detail_video_layout.setVisibility(View.GONE);
                others_video_detail_hint_layout.setVisibility(View.GONE);
                //提示框
                hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "你还未通过视频认证，无法查看该用户认证视频", "", "确认", gobackCallListener);
                if (!isFinishing()) {
                    hintdialog.show();
                }
                break;

            case 8:
                //提示框
                hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "对方尚未通过视频认证", "", "确认", gobackCallListener);
                if (!isFinishing()) {
                    hintdialog.show();
                }
                break;

            case 9:
                others_video_detail_video_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_layout.setVisibility(View.VISIBLE);
                others_video_detail_hint_tv.setText("用户通过手机录制并上传一段简短的自拍视频，工作人员会在24小时之内对其进行审核。我们会努力保障用户的真实性。");
                //封面不模糊，视频不上锁


                if (!CheckUtil.isEmpty(videoImgUrl)) {
                    Glide.with(this).load(videoImgUrl).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            others_video_detail_video_start_iv.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
                            others_video_detail_video_start_iv.setBackgroundResource(R.drawable.video_play_icon);
                            return false;
                        }
                    }).into(others_video_detail_coverimg_iv);
                } else {
                    others_video_detail_coverimg_iv.setVisibility(View.GONE);
                }

                if (CheckUtil.isEmpty(videoUrl)) {
                    LogUtils.e("视频地址空＝＝");
                    others_video_detail_video_layout.setVisibility(View.GONE);
                    others_video_detail_hint_layout.setVisibility(View.GONE);
                    hintdialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", "对方认证视频存在异常，暂时无法查看", "", "确认", gobackCallListener);
                    if (!isFinishing()) {
                        hintdialog.show();
                    }
                }
                break;
        }
    }

    private BackCallListener InviteOpenCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
                if (!isPostinvite) {
                    isPostinvite = true;
                    loadingDiaog = DialogUtil.LoadingDialog(OthersVideoDetailActivity.this, null);
                    if (!isFinishing()) {
                        loadingDiaog.show();
                    }
                    postVideoOpenInviteRequest(false);
                }
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
            }

        }
    };


    private BackCallListener VIPCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
            }

            //根据登陆用户的VIP状态跳转页面
            //若没有开通过VIP，点击进入VIP介绍页
            //若VIP已过期，点击进入续费页面

            Bundle bundle = new Bundle();

            int userPosition = DbHelperUtils.getOldVipPosition(LoginUser.getInstance().getUserId());


            bundle.putInt("userPosition", userPosition);

            bundle.putString(YpSettings.FROM_PAGE, "OthersVideoDetailActivity");

            if (0 == userPosition) {
                ActivityUtil.jump(OthersVideoDetailActivity.this, VipOpenedActivity.class, bundle, 0, 100);
            } else {
                ActivityUtil.jump(OthersVideoDetailActivity.this, VipRenewalsActivity.class, bundle, 0, 100);
            }

        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
            }
        }
    };

    private BackCallListener OpenAndVerificationgCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
                //去过度页面
                Bundle bundles = new Bundle();
                bundles.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());

                bundles.putString(YpSettings.FROM_PAGE, "OthersVideoDetailActivity");

                ActivityUtil.jump(OthersVideoDetailActivity.this, VideoDetailGetActivity.class, bundles, 0, 100);
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                hintdialog.dismiss();
            }
        }
    };

    private BackCallListener gobackCallListener = new BackCallListener() {
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


    /**
     * 视频邀请
     */
    private void postVideoOpenInviteRequest(boolean isconfirm) {


        InviteTypeBean typeBean = new InviteTypeBean();
        typeBean.setConfirm(isconfirm);
        typeBean.setInviteeId(userid);
        typeBean.setInviteType(3);

        InviteTypeService typeService = new InviteTypeService(this);
        typeService.parameter(typeBean);
        typeService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                InviteTypeRespBean typeRespBean = (InviteTypeRespBean) respBean;

                AttamptRespDto dto = typeRespBean.getResp();

                loadingDiaog.dismiss();

                isPostinvite = false;
                videoInvitePostResultHint(dto);
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                loadingDiaog.dismiss();

                isPostinvite = false;

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, msg);
            }
        });
        typeService.enqueue();


    }

    /**
     * 视频邀请结果判断显示提示dialog
     *
     * @param dto
     */
    private void videoInvitePostResultHint(AttamptRespDto dto) {

        if (dto != null) {
            if (dto.getViewStatus() == 0) {
                if (!CheckUtil.isEmpty(dto.getMessage())) {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, dto.getMessage());
                } else {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, "邀请失败");
                }
            } else if (dto.getViewStatus() == 1) {
                if (!CheckUtil.isEmpty(dto.getMessage())) {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, dto.getMessage());
                } else {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, "邀请成功");
                }

            } else if (dto.getViewStatus() == 2) {
                //需要P果
                invite_p_dialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", dto.getMessage(), "取消", "邀请", videobackCallListener);
                if (!isFinishing()) {
                    invite_p_dialog.show();
                }
            } else if (dto.getViewStatus() == 3) {
                //已经邀请
                if (!CheckUtil.isEmpty(dto.getMessage())) {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, dto.getMessage());
                } else {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, "已经邀请过了");
                }

            } else if (dto.getViewStatus() == 4) {

                invite_p_dialog = DialogUtil.createHintOperateDialog(OthersVideoDetailActivity.this, "", dto.getMessage(), "取消", "立即认证", VerficationBackCallListener);
                if (!isFinishing()) {
                    invite_p_dialog.show();
                }

            }
        }
    }

    private BackCallListener VerficationBackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                invite_p_dialog.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
            ActivityUtil.jump(OthersVideoDetailActivity.this, VideoDetailGetActivity.class, bundle, 0, 100);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                invite_p_dialog.dismiss();
            }
        }
    };

    private BackCallListener videobackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                invite_p_dialog.dismiss();
            }
            loadingDiaog = DialogUtil.LoadingDialog(OthersVideoDetailActivity.this, null);
            if (!isFinishing()) {
                loadingDiaog.show();
            }
            postVideoOpenInviteRequest(true);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                invite_p_dialog.dismiss();
            }

        }
    };

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
                    if (!VideoFileUtils.saveMyVideoFile(OthersVideoDetailActivity.this, bytes, filename)) {
                        isgetVideo = false;
                        DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, "未知错误");
                        others_video_detail_video_loading_pb.setVisibility(View.GONE);
                        others_video_detail_coverimg_iv.setVisibility(View.VISIBLE);
                        others_video_detail_video_start_iv.setVisibility(View.VISIBLE);
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
        RxBus.get().unregister(this);
        if (mp.isPlaying()) {
            mp.stop();  //停止播放视频
        }
        mp.release();   //释放资源
        DeletevideoExistsLocal();
        super.onDestroy();
    }


    private void get_video_detail() {


        loadingDiaog = DialogUtil.LoadingDialog(OthersVideoDetailActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        VedioDetailBean detailBean = new VedioDetailBean();
        detailBean.setUserId(userid);
        detailBean.setVisitorId(LoginUser.getInstance().getUserId());

        VedioDetailService detailService = new VedioDetailService(this);
        detailService.parameter(detailBean);
        detailService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                VedioDetailRespBean detailRespBean = (VedioDetailRespBean) respBean;
                VideoDetailDto dto = detailRespBean.getResp();

                loadingDiaog.dismiss();
                try {

                    if (dto != null) {
                        if (dto.getViewStatus() != 0 && dto.getViewStatus() != 1 && dto.getViewStatus() != 2) {


                            vediostatus = dto.getViewStatus();
                            if (dto.getVerification() != null) {
                                videoImgUrl = dto.getVerification().getCoverImgUrl();
                                videoUrl = dto.getVerification().getVideoUrl();
                            }

                            setDateToView();
                        }

                    } else {
                        DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this);
                    finish();
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this);
                    finish();
                    return;
                }
                DialogUtil.showDisCoverNetToast(OthersVideoDetailActivity.this, msg);
                finish();
            }
        });

        detailService.enqueue();

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("VideoLookLimitChangeEvent")

            }
    )
    public void videoLookLimitChange(VideoLookLimitChangeEvent event) {
        if (event != null) {

            get_video_detail();
        }
    }

}
