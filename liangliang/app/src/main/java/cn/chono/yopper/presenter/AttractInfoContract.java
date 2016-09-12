package cn.chono.yopper.presenter;

import java.util.List;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;
import cn.chono.yopper.entity.charm.ReceiveGiftInfoEntity;

/**
 * Created by sunquan on 16/8/4.
 */
public interface AttractInfoContract {

    interface View extends IView {

        void setCharmNumView(int charm);

        void setAccountView(int account);

        void showError();

        void updateGiftListView(List<ReceiveGiftInfoEntity> list);

        void loadMoreGiftListView(List<ReceiveGiftInfoEntity> list);

        void showErrorHint(String msg);

        void showNoData();


        void stopRefresh();
        void setLoadcomplete(boolean b);
        void stopLoadMore(boolean b);
        void stopLoadMore();

    }

    interface Presenter extends IPresenter {

        void getAttractInfo(int start);

        void loadMoreGift();
    }
}
