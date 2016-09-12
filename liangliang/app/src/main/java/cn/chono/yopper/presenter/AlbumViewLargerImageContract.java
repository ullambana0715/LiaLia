package cn.chono.yopper.presenter;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.utils.BackCallListener;

/**
 * Created by cc on 16/8/4.
 */
public interface AlbumViewLargerImageContract {


    interface View extends IView {

        void showCreateHintOperateDialog(String title, String content, String msg1, String msg2, BackCallListener backCallListener);


        void dismissCreateHintOperateDialog();


        void setAdapterData(Object o,int position,int userId, int loginUserId);

        void showDisCoverNetToast(String msg);


    }


    interface Presenter extends IPresenter {

        void initDataAndLoadData(Object o, int position, int userId);


    }
}
