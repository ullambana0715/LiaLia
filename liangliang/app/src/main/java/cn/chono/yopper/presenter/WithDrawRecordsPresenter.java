package cn.chono.yopper.presenter;

import android.app.Activity;

import com.orhanobut.logger.Logger;

import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.utils.SchedulersCompat;
import rx.Subscription;

/**
 * Created by sunquan on 16/8/4.
 */
public class WithDrawRecordsPresenter extends BasePresenter<WithDrawRecordsContract.View> implements WithDrawRecordsContract.Presenter {

    public WithDrawRecordsPresenter(Activity activity, WithDrawRecordsContract.View view) {
        super(activity, view);
    }

    @Override
    public void getWithDrawRecords(int userID, int start) {

        showLoading();
        Subscription subscription = HttpFactory.getHttpApi().getWithDrawRecords(userID, 0)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(dto -> {
                    dismissLoadingDiaog();
                    if (dto != null) {
                        if (dto.getList() != null && dto.getList().size() > 0) {
                            mView.updateWithDrawRecordsView(dto.getList());
                        } else {
                            mView.showNoData();
                        }
                    } else {
                        mView.showNoData();
                    }
                }, throwable -> {
                    dismissLoadingDiaog();
                    mView.showError();
                });
        addSubscrebe(subscription);
    }
}
