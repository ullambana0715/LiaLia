package cn.chono.yopper.presenter;

import cn.chono.yopper.base.IPresenter;
import cn.chono.yopper.base.IView;

/**
 * Created by sunquan on 16/8/4.
 */
public interface WithDrawContract {

    interface View extends IView {

        void updateCharmValue(int charmValue);

        void updateWithDrawAccount(int accunt);

        void showToastHint(String msg);

        void commitDialog(int cash,String aliPayAccount,String aliPayAccountName);

        void withDrawLimit();

        void withDrawtMorethanHintDialog(String msg);

        void withDrawSuccess();

        void isCanInput(int account);
    }

    interface Presenter extends IPresenter {

        void commitWithDraw(int userId,int cash,String aliPayAccount,String aliPayAccountName);
    }
}
