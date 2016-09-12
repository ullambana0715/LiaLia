package cn.chono.yopper.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;

import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.BasePresenter;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.charm.ReceiveGiftListEntity;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.SchedulersCompat;
import rx.Subscription;

/**
 * Created by sunquan on 16/8/4.
 */
public class AttractInfoPresenter extends BasePresenter<AttractInfoContract.View> implements AttractInfoContract.Presenter {


    @Override
    public void detachView() {
        super.detachView();
        RxBus.get().unregister(this);
    }

    public AttractInfoPresenter(Activity activity, AttractInfoContract.View view) {
        super(activity, view);
        RxBus.get().register(this);
    }


    @Subscribe(

            thread = EventThread.MAIN_THREAD,

            tags = {


                    @Tag("WithDrawEvent")
            }

    )
    public void WithDrawEvent(CommonEvent commonEvent) {


        getAttractInfo(0);


    }


    public String nextQuery;

    @Override
    public void getAttractInfo(int start) {

        showLoading();

        Subscription subscription =mHttpApi.getAttractInfo(0)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(dto -> {

                    mView.stopRefresh();

                    dismissLoadingDiaog();

                    if (dto != null) {

                        mView.setAccountView(dto.getRemainCharmFee());

                        mView.setCharmNumView(dto.getRemainCharm());

                        ReceiveGiftListEntity receiveGiftListEntity = dto.getMyGifts();

                        if (receiveGiftListEntity != null && receiveGiftListEntity.getList() != null && receiveGiftListEntity.getList().size() > 0) {

                            mView.updateGiftListView(receiveGiftListEntity.getList());

                            nextQuery = receiveGiftListEntity.getNextQuery();

                            Logger.e("下一页" + nextQuery);

                        }else{

                            mView.showNoData();

                        }
                    }


                    if (TextUtils.isEmpty(nextQuery)) {

                        mView.setLoadcomplete(true);

                    } else {

                        mView.setLoadcomplete(false);
                    }

                    mView.stopLoadMore(false);

//


                }, throwable -> {

                    mView.stopRefresh();

                    dismissLoadingDiaog();

                    mView.showError();

                });

        addSubscrebe(subscription);
    }

    @Override
    public void loadMoreGift() {

        if (CheckUtil.isEmpty(nextQuery)) {

            mView.setLoadcomplete(true);

            mView.stopLoadMore(false);

            return;
        }

        Subscription subscription = mHttpApi.loadMoreGift(nextQuery)

                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(dto -> {

                    dismissLoadingDiaog();

                    if (dto != null) {

                        mView.setAccountView(dto.getRemainCharmFee());

                        mView.setCharmNumView(dto.getRemainCharm());


                        ReceiveGiftListEntity receiveGiftListEntity = dto.getMyGifts();

                        if (receiveGiftListEntity != null && receiveGiftListEntity.getList() != null && receiveGiftListEntity.getList().size() > 0) {

                            mView.loadMoreGiftListView(receiveGiftListEntity.getList());

                            nextQuery = receiveGiftListEntity.getNextQuery();


                        }
                    }


                    if (TextUtils.isEmpty(nextQuery)) {

                        mView.setLoadcomplete(true);

                        mView.stopLoadMore(false);

                    } else {
                        mView.setLoadcomplete(false);
                        mView.stopLoadMore();
                    }

                }, throwable -> {

                    dismissLoadingDiaog();

                    ApiResp apiResp = ErrorHanding.handle(throwable);

                    if (apiResp != null) {

                        mView.showErrorHint(apiResp.getMsg());

                    } else {

                        mView.showErrorHint("网络异常");
                    }

                    mView.setLoadcomplete(false);

                    mView.stopLoadMore(false);

                });

        addSubscrebe(subscription);
    }
}
