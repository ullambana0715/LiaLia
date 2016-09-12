package cn.chono.yopper.presenter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileBean;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileService;
import cn.chono.yopper.Service.OKHttpListener;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.RePortCallListener;
import cn.chono.yopper.utils.SHA;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.video.VideoFileUtils;
import rx.Subscription;

/**
 * Created by cc on 16/7/30.
 */
public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {

    @Override
    public void detachView() {
        super.detachView();
        RxBus.get().unregister(this);
    }

    public VideoPresenter(Activity activity, VideoContract.View view) {
        super(activity, view);

        RxBus.get().register(this);

        loginUserId = LoginUser.getInstance().getUserId();

        mp = new MediaPlayer();
    }

    MediaPlayer mp;     //声明MediaPlayer对象


    GeneralVideos generalVideos;


    int position;

    int userId;

    int loginUserId;

    boolean isgetVideo = false;

    String type;

    SurfaceView mVideoSv;


    @Override
    public void initDataAndLoadData( SurfaceView mVideoSv) {


        Bundle bundle = mActivity.getIntent().getExtras();

        if (bundle.containsKey("Data"))

            generalVideos = (GeneralVideos) bundle.getSerializable("Data");

        if (bundle.containsKey("position"))

            position = bundle.getInt("position");

        if (bundle.containsKey("type"))

            type = bundle.getString("type");


        if (bundle.containsKey(YpSettings.USERID))

            userId = bundle.getInt(YpSettings.USERID);


        this.mVideoSv = mVideoSv;

        if (TextUtils.equals("UserInfoPresenter", type)) {

            mView.video_ll_parise_deleteVisible();

            if (userId == loginUserId) {//只有删除，同时有点赞数量

                mView.parise_count_tvVisible();

                mView.parise_count_tv();


                mView.mPariseCountTv(generalVideos.getPraiseCount() + "");//点赞数量


                mView.iv_delectVisible();

                mView.iv_delect(R.drawable.ic_delect);


            } else {//只有点赞

                mView.parise_count_tvVisible();

                int number = generalVideos.getPraiseCount();

                if (generalVideos.getPraiseStatus() == 1) {//有点过

                    mView.parise_count_tv();

                } else {
                    mView.parise_count_tv_cancel();
                }

                mView.mPariseCountTv(number + "");


                mView.iv_delectVisible();

                mView.iv_delect(R.drawable.ic_blacklist);

            }


        } else {

            mView.video_ll_parise_deleteGone();

        }


        initVideoData();


    }


    private void initVideoData() {


        mp.setOnErrorListener((mp, what, extra) -> {


            mView.video_llGome();


            mView.showCreateHintOperateDialog("", "视频存在异常，暂时无法查看", "", "确认", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {
                    mView.dismissCreateHintOperateDialog();
                }

                @Override
                public void onEnsure(View view, Object... obj) {
                    mView.dismissCreateHintOperateDialog();

                    mActivity.finish();

                }
            });

            return true;
        });
        //为MediaPlayer对象添加完成事件监听器
        mp.setOnCompletionListener(mp -> {

            Logger.e("video=完成＝");

            mView.video_pbGone();

            mView.video_iv_coverimgVisible();

            mView.video_iv_startVisible();


            mp.reset();

        });


        mView.video_iv_coverimg(generalVideos.getCoverImgUrl());

        mView.video_iv_coverimgVisible();

        mView.video_pbVisible();


        String localpath = getvideoExistsLocalPath(generalVideos.getVideoUrl());

        Logger.e(localpath);

        if (!CheckUtil.isEmpty(localpath)) {

            playDeal();

        } else {

            if (isgetVideo) {
                return;
            }

            mView.video_iv_startGone();

            mView.video_pbVisible();

            isgetVideo = true;

            getMusicFile(generalVideos.getVideoUrl());

            return;
        }


    }


    @Override
    public void videoLlClick() {

        String localpath = getvideoExistsLocalPath(generalVideos.getVideoUrl());

        if (!CheckUtil.isEmpty(localpath)) {

            playDeal();

        } else {

            if (isgetVideo) {
                return;
            }

            mView.video_iv_startGone();

            mView.video_pbVisible();

            isgetVideo = true;

            getMusicFile(generalVideos.getVideoUrl());

            return;
        }


    }

    @Override
    public void VideoSvClick() {//暂停


        if (mp.isPlaying()) {

            mView.video_iv_coverimgVisible();

            mView.video_iv_startVisible();

            mView.video_pbGone();

            mp.pause();
        }

        mp.reset();


    }

    /**
     * 点赞
     */
    @Override
    public void pariseClick() {


        if (userId == loginUserId) {

            return;
        }

        boolean isPareis;

        int umber = generalVideos.getPraiseCount();

        if (generalVideos.getPraiseStatus() == 1) {

            generalVideos.setPraiseStatus(2);

            umber = umber - 1;

            mView.parise_count_tv_cancel();

            isPareis = true;//这里是要点赞
        } else {

            generalVideos.setPraiseStatus(1);

            umber = umber + 1;

            mView.parise_count_tv();

            isPareis = false; //这里是要取消点赞
        }

        if (umber < 0) {

            umber = 0;
        }

        generalVideos.setPraiseCount(umber);

        mView.mPariseCountTv(umber + "");


        CommonItemEvent commonItemEvent = new CommonItemEvent();

        commonItemEvent.event = generalVideos;

        commonItemEvent.position = position;

        RxBus.get().post("PariseVideoEvent", commonItemEvent);


        Subscription subscription = mHttpApi.putPraise(generalVideos.getVideoUrl(), isPareis)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(b -> {


                }, throwable -> {


//                    ApiResp apiResp = ErrorHanding.handle(throwable);
//
//                    if (apiResp == null) {
//
//
//                        mView.showDisCoverNetToast(null);
//                    } else {
//
//                        mView.showDisCoverNetToast(apiResp.getMsg());
//
//                    }


                });

        addSubscrebe(subscription);


    }

    /**
     * 删除 与 举报
     */
    @Override
    public void delectClick() {

        if (userId == loginUserId) {//删除


            mView.showCreateHintOperateDialog("", "是否删除？", "取消", "确认", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {
                    mView.dismissCreateHintOperateDialog();


                }

                @Override
                public void onEnsure(View view, Object... obj) {

                    mView.dismissCreateHintOperateDialog();

                    Subscription subscription = mHttpApi.deleteVideo(loginUserId, generalVideos.getVideoId())

                            .compose(SchedulersCompat.applyIoSchedulers())

                            .compose(RxResultHelper.handleResult())

                            .subscribe(b -> {


                                if (b) {


                                    CommonItemEvent commonItemEvent = new CommonItemEvent();

                                    commonItemEvent.event = generalVideos;

                                    commonItemEvent.position = position;

                                    RxBus.get().post("DelectVideoEvent", commonItemEvent);

                                    mActivity.finish();

                                } else {

                                    mView.showDisCoverNetToast("删除失败");
                                }


                            }, throwable -> {

                                ApiResp apiResp = ErrorHanding.handle(throwable);

                                if (apiResp == null) {


                                    mView.showDisCoverNetToast(null);

                                } else {

                                    mView.showDisCoverNetToast(apiResp.getMsg());

                                }


                            });

                    addSubscrebe(subscription);


                }
            });


        } else {//举报

            mView.showRePortDialog("举报原因", "诽谤谩骂", "色情骚扰", "垃圾广告", "欺诈（酒托、饭托等）", "违法（涉毒、暴恐等）", new RePortCallListener() {
                @Override
                public void onOne(View view, Object... obj) {

                    mView.dismissRePortDialog();

                    doReport("诽谤谩骂");

                }

                @Override
                public void onTwo(View view, Object... obj) {


                    mView.dismissRePortDialog();

                    doReport("色情骚扰");

                }

                @Override
                public void onThree(View view, Object... obj) {


                    mView.dismissRePortDialog();

                    doReport("垃圾广告");

                }

                @Override
                public void onFour(View view, Object... obj) {


                    mView.dismissRePortDialog();

                    doReport("欺诈（酒托、饭托等）");

                }

                @Override
                public void onFive(View view, Object... obj) {


                    mView.dismissRePortDialog();

                    doReport("违法（涉毒、暴恐等）");

                }
            });


        }


    }


    private void playDeal() {

        boolean isPlaying = false;

        try {

            isPlaying = mp.isPlaying();

        } catch (IllegalStateException e) {

        }

        if (isPlaying) {//暂停

            mView.video_pbGone();

            mView.video_iv_coverimgGone();

            mView.video_iv_startVisible();


            mp.pause();


        } else {//播放

            try {

                mView.video_iv_startGone();

                mView.video_pbGone();

                setVideoData(generalVideos.getVideoUrl());

            } catch (Exception e) {
                Logger.e("playDeal=" + e.getMessage());
                e.printStackTrace();
            }


        }
    }


    private void setVideoData(String videoUrl) {

        String localpath = getvideoExistsLocalPath(videoUrl);

        Logger.e("video=" + localpath);

        if (!CheckUtil.isEmpty(localpath)) {

            try {

                mView.video_iv_coverimgGone();

                mp.setDataSource(localpath);

                SurfaceHolder holder = mVideoSv.getHolder();

                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


                holder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                    }
                });


                mp.setDisplay(holder);  //设置将视频画面输出到SurfaceView


                mp.prepare(); //预加载视频

                mp.start(); //开始播放


            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {

            getMusicFile(videoUrl);

        }
    }


    private String getvideoExistsLocalPath(String videoUrl) {

        final String filename = SHA.encodeByMD5(videoUrl);

        if (VideoFileUtils.isMyVideoFileExists(mActivity, filename)) {

            File file = null;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                file = new File(Environment.getExternalStorageDirectory() + File.separator + "video" + File.separator + "myvideo" + File.separator + filename + ".mp4");

            } else {

                file = new File(mActivity.getFilesDir() + File.separator + "video" + File.separator + "myvideo" + File.separator + filename + ".mp4");
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


    @Override
    public void startVideo() {

    }

    @Override
    public void onResume() {

//        setVideoData();

    }

    @Override
    public void onPause() {


        if (mp.isPlaying()) {

            mView.video_iv_coverimgVisible();

            mView.video_iv_startVisible();


            mp.pause();
        }

        mp.reset();

    }

    @Override
    public void onDestroy() {

        if (mp != null) {
            if (mp.isPlaying()) {

                mp.stop();  //停止播放视频
            }
            mp.reset();
            mp.release();   //释放资源

        }


    }

    @Override
    public void OnFinish() {


//        if (TextUtils.equals("UserInfoPresenter", type)) {
//
//            CommonItemEvent commonItemEvent = new CommonItemEvent();
//
//            commonItemEvent.event = generalVideos;
//
//            RxBus.get().post("UserInfoActivityVideoEvent", commonItemEvent);
//        }


    }

    @Override
    public void onKeyDown() {
        if (mp != null) {
            if (mp.isPlaying()) {

                mp.stop();  //停止播放视频
            }
            mp.reset();
            mp.release();   //释放资源

        }
    }


    /**
     * 下载视频
     *
     * @param url
     */
    private void getMusicFile(final String url) {


        if (TextUtils.isEmpty(url)) {

            mView.showDisCoverNetToast("未知错误");

            return;
        }


        final String filename = SHA.encodeByMD5(url);

        DownloadMusicFileBean musicFileBean = new DownloadMusicFileBean();

        musicFileBean.setUrl(url);

        DownloadMusicFileService fileService = new DownloadMusicFileService(mActivity);

        fileService.setOKHttpListener(new OKHttpListener() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);

                byte[] bytes = (byte[]) result;

                if (bytes != null && bytes.length > 0) {

                    if (!VideoFileUtils.saveMyVideoFile(mActivity, bytes, filename)) {

                        isgetVideo = false;

                        mView.showDisCoverNetToast("未知错误");

                        mView.video_pbGone();

                        mView.video_iv_coverimgVisible();

                        mView.video_iv_startVisible();


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

                mView.showDisCoverNetToast("网络异常，请稍后重试");

                mView.video_pbGone();

                mView.video_iv_coverimgVisible();

                mView.video_iv_startVisible();

            }
        });

        fileService.parameter(musicFileBean);

        fileService.enqueue();

//
    }


    /**
     * 举报
     *
     * @param msg
     */

    public void doReport(String msg) {


        Map<String, Object> map = new HashMap<>();

        map.put("type", 6);

        map.put("Id", userId);

        map.put("Content", msg);


        Subscription subscription = mHttpApi.postReport(map)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(report -> {


                    mView.showTimerCreateSuccessHintDialog("举报成功!");

                    successtimer = new SuccessTimer(2000, 1000);

                    successtimer.start();

                }, throwable -> {


                    ApiResp apiResp = ErrorHanding.handle(throwable);


                    if (apiResp == null) {


                        mView.showDisCoverNetToast(null);
                    } else {

                        mView.showDisCoverNetToast(apiResp.getMsg());

                    }


                });

        addSubscrebe(subscription);

    }


    SuccessTimer successtimer;

    private class SuccessTimer extends CountDownTimer {

        public SuccessTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            mView.dismissTimerCreateSuccessHintDialog();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


}
