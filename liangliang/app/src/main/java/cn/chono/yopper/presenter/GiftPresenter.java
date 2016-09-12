package cn.chono.yopper.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.App;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.chatgift.GiftOrderResp;
import cn.chono.yopper.entity.chatgift.GiftOrdreReq;
import cn.chono.yopper.entity.chatgift.GiveGiftBody;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.GiftEvent;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SchedulersCompat;
import rx.Subscription;

/**
 * Created by sunquan on 16/8/4.
 */
public class GiftPresenter extends BasePresenter<GiftContract.View> implements GiftContract.Presenter {

    public GiftPresenter(Activity activity, GiftContract.View view) {
        super(activity, view);
    }

    @Override
    public void getAllGifts() {

        Subscription subscription = HttpFactory.getHttpApi().getAllGifts()
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(giftBean -> {
                    if (giftBean != null) {

                        if (giftBean.getGiftsOfPassHot() != null && giftBean.getGiftsOfPassHot().size() > 0) {

                            mView.updateGiftPassHotList(giftBean.getGiftsOfPassHot());

                        } else {

                            mView.showNoGiftPassHotDataView();
                        }

                        if (giftBean.getGiftsOfUnpassHot() != null && giftBean.getGiftsOfUnpassHot().size() > 0) {

                            mView.updateGiftNoPassList(giftBean.getGiftsOfUnpassHot());
                        }

                        mView.updateAppleCountView(giftBean.getRemainAppleCount());

                    } else {
                        mView.showNoGiftPassHotDataView();
                    }

                }, throwable -> {

//                    mView.showNoGiftPassHotDataView();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {

                        DialogUtil.showDisCoverNetToast(mActivity);

                    } else {

                        DialogUtil.showDisCoverNetToast(mActivity, apiResp.getMsg());

                    }

                });

        addSubscrebe(subscription);
    }


    @Override
    public void giveGift(String giftID, String datingId, int toUserId, boolean isFirstCall, String giftImg, int charmValue) {

        GiveGiftBody body = new GiveGiftBody();
        body.setDatingId(datingId);
        body.setIsFirstCall(isFirstCall);
        body.setToUserId(toUserId);

        Subscription subscription = HttpFactory.getHttpApi().giveGift(giftID, body)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(dto -> {
                    // 赠送结果（0：成功 1：对方Hot 2：苹果数量不足）\r\n   3：存在拉黑关系         当对方Hot时，app端组织提示文案
                    if (dto != null) {

                        if (dto.getResult() == 0) {
                            RxBus.get().post("giveGiftSuccess", new GiftEvent(giftImg, charmValue, dto.getToSendWords()));
                        } else if (dto.getResult() == 1) {
                            RxBus.get().post("giveGiftFiledHot", new CommonEvent<>());
                        } else if (dto.getResult() == 2) {
                            RxBus.get().post("appleInsufficient", new CommonEvent<>(dto));
                        } else if (dto.getResult() == 3) {
                            RxBus.get().post("sendGiftWithBolck", new CommonEvent<>(dto.getMsg()));
                        } else if (dto.getResult() == 4) {
                            RxBus.get().post("sendGiftWithRefused", new CommonEvent<>(dto.getMsg()));
                        }
                    }

                }, throwable -> {

//                    mView.showNoGiftPassHotDataView();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {

                        DialogUtil.showDisCoverNetToast(mActivity);

                    } else {

                        DialogUtil.showDisCoverNetToast(mActivity, apiResp.getMsg());

                    }

                });

        addSubscrebe(subscription);
    }

    @Override
    public void appleQuickly(GiftOrdreReq rep) {
        Subscription subscription = HttpFactory.getHttpApi().giveOrder(rep)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(orderResp -> {
                    GiftOrderResp resp = orderResp;
                    if (resp.getResult() == 0) {

                        RxBus.get().post("quicklySuccess", resp);

                    } else if (resp.getResult() == 1) {
                        RxBus.get().post("quicklyFiledWithNoApple", resp);
                    }

                }, throwable -> {
                    RxBus.get().post("quicklyFiled", 1);

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp == null) {

                        DialogUtil.showDisCoverNetToast(mActivity);

                    } else {

                        DialogUtil.showDisCoverNetToast(mActivity, apiResp.getMsg());

                    }

                });

        if (subscription != null) {
            addSubscrebe(subscription);
        }
    }

}
