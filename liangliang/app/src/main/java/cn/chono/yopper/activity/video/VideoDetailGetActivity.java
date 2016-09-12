package cn.chono.yopper.activity.video;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.VedioDetail.VedioDetailBean;
import cn.chono.yopper.Service.Http.VedioDetail.VedioDetailRespBean;
import cn.chono.yopper.Service.Http.VedioDetail.VedioDetailService;

import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.VideoDetailDto;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;


/**
 * 视频详情获取过度页面 通过获取到的数据状态 判断进入他人视频还是自己视频详情还是去视频认证引导页
 * <p>
 * * @author sam.sun
 */

public class VideoDetailGetActivity extends MainFrameActivity {

    private LayoutInflater mInflater;
    private View contextView;

    private Dialog loadingDiaog;

    private boolean isPost = false;

    private int VisitorId, userID,videoId;

    private String frompage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.video_get_activity, null);
        setContentView(contextView);
        VisitorId = LoginUser.getInstance().getUserId();

        Bundle bunble = this.getIntent().getExtras();
        if (bunble != null) {
            userID = bunble.getInt(YpSettings.USERID);
            videoId = bunble.getInt(YpSettings.VIDEO_ID);
            if(bunble.containsKey(YpSettings.FROM_PAGE)){
                frompage=bunble.getString(YpSettings.FROM_PAGE);
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isPost) {
            isPost = true;
            loadingDiaog = DialogUtil.LoadingDialog(VideoDetailGetActivity.this, null);
            if (!isFinishing()) {
                loadingDiaog.show();
            }
//            if (videoId != 0){
//
//            }else {
                get_video_detail();
//            }
        }
    }


    private void get_video_detail() {


        VedioDetailBean detailBean = new VedioDetailBean();
        detailBean.setUserId(userID);
        detailBean.setVisitorId(VisitorId);

        VedioDetailService detailService = new VedioDetailService(this);
        detailService.parameter(detailBean);
        detailService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                VedioDetailRespBean detailRespBean = (VedioDetailRespBean) respBean;
                VideoDetailDto dto = detailRespBean.getResp();


                isPost = false;
                loadingDiaog.dismiss();
                try {

                    if (dto != null) {
                        if (dto.getViewStatus() == 0) {
                            //自己的视频 未认证 或者未通过 跳转到视频认证引导页面
                            ActivityUtil.jump(VideoDetailGetActivity.this, VideoWelcomeActivity.class, null, 0, 100);
                            finish();
                        } else if (dto.getViewStatus() == 1 || dto.getViewStatus() == 2) {
                            //自己的视频 审核中 或者已通过 去自己的视频详情页
                            Bundle bundles = new Bundle();
                            bundles.putInt(YpSettings.VIDEO_STATE, dto.getViewStatus());

                            if (dto.getVerification() != null) {
                                bundles.putString(YpSettings.VIDEO_IMG_URl, dto.getVerification().getCoverImgUrl());
                                bundles.putString(YpSettings.VIDEO_URL, dto.getVerification().getVideoUrl());
                                bundles.putInt(YpSettings.VIDEO_PURPOSE, dto.getVerification().getPurpose());
                                bundles.putBoolean(YpSettings.VIDEO_OPEN, dto.getVerification().isOpen());
                                bundles.putBoolean(YpSettings.VIDEO_CHAT_WITH_USER_ONLY, dto.getVerification().isChatWithVideoUserOnly());
                            }
                            if(!CheckUtil.isEmpty(frompage)){
                                bundles.putString(YpSettings.FROM_PAGE, frompage);
                            }
                            ActivityUtil.jump(VideoDetailGetActivity.this, VideoDetailActivity.class, bundles, 0, 100);
                            finish();
                        } else {
                            //去他人视频详情页面
                            Bundle bundles = new Bundle();
                            bundles.putInt(YpSettings.USERID, userID);

                            bundles.putInt(YpSettings.VIDEO_STATE, dto.getViewStatus());
                            if (dto.getVerification() != null) {
                                bundles.putString(YpSettings.VIDEO_IMG_URl, dto.getVerification().getCoverImgUrl());
                                bundles.putString(YpSettings.VIDEO_URL, dto.getVerification().getVideoUrl());
                            } else {
                                bundles.putString(YpSettings.VIDEO_IMG_URl, "");
                                bundles.putString(YpSettings.VIDEO_URL, "");
                            }
                            ActivityUtil.jump(VideoDetailGetActivity.this, OthersVideoDetailActivity.class, bundles, 0, 100);
                            finish();
                        }
                    } else {
                        DialogUtil.showDisCoverNetToast(VideoDetailGetActivity.this);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showDisCoverNetToast(VideoDetailGetActivity.this);
                    finish();
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                isPost = false;
                loadingDiaog.dismiss();
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(VideoDetailGetActivity.this);
                    finish();
                    return;
                }
                DialogUtil.showDisCoverNetToast(VideoDetailGetActivity.this, msg);
                finish();
            }
        });

        detailService.enqueue();

    }

}
