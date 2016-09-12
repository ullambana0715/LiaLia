package cn.chono.yopper.presenter;

import java.util.List;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.entity.WithDrawItemEntity;

/**
 * Created by sunquan on 16/8/4.
 */
public interface WithDrawRecordsContract {

    interface View extends IView {

        void updateWithDrawRecordsView(List<WithDrawItemEntity> list);

        void showNoData();

        void showError();



    }

    interface Presenter extends IPresenter {

        void getWithDrawRecords(int userID,int start);
    }
}
