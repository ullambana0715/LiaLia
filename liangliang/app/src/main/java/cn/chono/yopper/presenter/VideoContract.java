package cn.chono.yopper.presenter;

import android.view.SurfaceView;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.RePortCallListener;

/**
 * Created by cc on 16/7/21.
 */
public interface VideoContract {


    interface View extends IView {







        void video_llGome();

        void video_llVisible();

        void video_iv_coverimgGone();

        void video_iv_coverimgVisible();

        void video_pbGone();

        void video_pbVisible();

        void video_iv_startGone();

        void video_iv_startVisible();

        void parise_count_tvGone();

        void parise_count_tvVisible();

        void video_ll_parise_deleteGone();

        void video_ll_parise_deleteVisible();

        void iv_delectGone();

        void iv_delectVisible();

        void iv_delect(int id);

        void video_iv_coverimg(String msg);

        void video_iv_start(Object msg);

        void showCreateHintOperateDialog(String title, String msg, String msg1, String msg2, BackCallListener backCallListener);

        void dismissCreateHintOperateDialog();

        void showDisCoverNetToast(String msg);

        void showTimerCreateSuccessHintDialog(String msg);

        void dismissTimerCreateSuccessHintDialog();

        void showRePortDialog(String title, String one, String two, String three, String four, String five, RePortCallListener rePortCallListener);

        void dismissRePortDialog();

        void parise_count_tv();

        void parise_count_tv_cancel();

        void mPariseCountTv(String msg);



    }


    interface Presenter extends IPresenter {

        void initDataAndLoadData(SurfaceView mVideoSv);

        void videoLlClick();

        void VideoSvClick();

        void pariseClick();

        void delectClick();



        void startVideo();

        void onResume();

        void onPause();

        void onDestroy();

        void OnFinish();

        void onKeyDown();


    }

}
