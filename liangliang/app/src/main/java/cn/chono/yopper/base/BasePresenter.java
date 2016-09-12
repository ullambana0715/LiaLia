package cn.chono.yopper.base;

import android.app.Activity;
import android.app.Dialog;

import cn.chono.yopper.api.HttpApi;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.utils.DialogUtil;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 *
 */
public abstract class BasePresenter<T extends IView> implements IPresenter {

    protected Activity mActivity;
    protected T mView;
    protected CompositeSubscription mCompositeSubscription;
    protected static final HttpApi mHttpApi = HttpFactory.getHttpApi();

    private Dialog loadingDiaog;

    public BasePresenter(Activity activity, T view) {
        this.mActivity = activity;
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }


    protected void showLoading(String text) {
        loadingDiaog=DialogUtil.LoadingDialog(mActivity,text);
        if (!mActivity.isFinishing()) {
            loadingDiaog.show();
        }
    }

    protected void showLoading() {
        loadingDiaog=DialogUtil.LoadingDialog(mActivity);
        if (!mActivity.isFinishing()) {
            loadingDiaog.show();
        }
    }


    protected void dismissLoadingDiaog() {

        if(loadingDiaog!=null){
            loadingDiaog.dismiss();
        }

    }

    protected void handleError(Throwable throwable) {
        DialogUtil.showDisCoverNetToast(mActivity, ErrorHanding.handleError(throwable));
    }


    protected void handleErrorHint(String msg) {
        DialogUtil.showDisCoverNetToast(mActivity, msg);
    }


    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
