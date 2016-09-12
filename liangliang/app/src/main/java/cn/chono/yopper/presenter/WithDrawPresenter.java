package cn.chono.yopper.presenter;

import android.app.Activity;

import com.hwangjr.rxbus.RxBus;

import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.entity.WithDrawBody;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.utils.SchedulersCompat;
import rx.Subscription;

/**
 * Created by sunquan on 16/8/4.
 */
public class WithDrawPresenter extends BasePresenter<WithDrawContract.View> implements WithDrawContract.Presenter {

    public WithDrawPresenter(Activity activity, WithDrawContract.View view) {
        super(activity, view);
    }

    @Override
    public void commitWithDraw(int userId, int cash, String aliPayAccount, String aliPayAccountName) {

        WithDrawBody withDrawBody = new WithDrawBody(cash, aliPayAccount, aliPayAccountName);

        Subscription subscription = mHttpApi.withDrawCommit(userId, withDrawBody)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(dto -> {
                    if (dto != null) {

                        if (dto.getResult() == 0) {
                            //成功

                            RxBus.get().post("WithDrawEvent", new CommonEvent<>());

                            mView.withDrawSuccess();

                        } else if (dto.getResult() == 1) {
                            //金额太少

                            mView.showToastHint(dto.getMsg());

                        } else if (dto.getResult() == 2) {
                            //魅力值不足

                            mView.showToastHint(dto.getMsg());

                        } else if (dto.getResult() == 3) {
                            //超过每日上限
                            mView.withDrawtMorethanHintDialog(dto.getMsg());
                        } else if (dto.getResult() == 4) {
                            //已超过每日上限
                            mView.withDrawtMorethanHintDialog(dto.getMsg());
                        }

                    }

                }, throwable -> {


                });

        addSubscrebe(subscription);
    }
}
