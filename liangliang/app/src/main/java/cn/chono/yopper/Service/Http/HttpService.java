package cn.chono.yopper.Service.Http;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.andbase.tractor.http.CallWrap;
import com.orhanobut.logger.Logger;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.OKHttpListener;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.task.RemoveAliasAsyncTask;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;

/**
 * Created by zxb on 2015/11/19.
 */
public abstract class HttpService {
    public HttpService(Context context) {
        this.context = context;
    }


    protected Context context;
    protected OnCallBackSuccessListener onCallBackSuccessListener;
    protected OnCallBackFailListener onCallBackFailListener;
    protected Class OutDataClass = RespBean.class;
    protected CallWrap callWrap;

    protected OKHttpListener okHttpListener = new OKHttpListener() {
        @Override
        public void onSuccess(Object result) {
            super.onSuccess(result);


            try {

                RespBean respBean = (RespBean) JsonUtils.fromJson(result.toString(), OutDataClass);

                if (null == respBean) {
                    Logger.e("卧槽请求回来了是空的");
                    onError(null);
                    return;
                }


                if (TextUtils.equals("401", respBean.getStatus()) || TextUtils.equals("403", respBean.getStatus())) {

                    Logger.e("卧槽请求回来了"+respBean.getStatus());


                    String authToken = LoginUser.getInstance().getAuthToken();

                    if (!TextUtils.isEmpty(authToken)) {
                        RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();
                        loginAsyncTask.execute(respBean.getMsg());
                        return;
                    }

                }


                if (TextUtils.equals("200", respBean.getStatus())) {

                    onCallSucceed(respBean);
                    if (null != onCallBackSuccessListener)
                        onCallBackSuccessListener.onSuccess(respBean);
                    return;
                }


                if (TextUtils.equals("500", respBean.getStatus())) {
                    respBean.setMsg(context.getResources().getString(R.string.network_errer));
                }


                Logger.e("卧槽请求回来了错误");

                onError(respBean);

            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                onError(null);
            }

        }

        @Override
        public void onStart() {
            super.onStart();

        }

        @Override
        public void onCancelClick() {
            super.onCancelClick();
        }

        @Override
        public void onLoading(Object result) {
            super.onLoading(result);

        }

        @Override
        public void onFail(Object result) {
            super.onFail(result);

            Logger.e("卧槽请求回来了＝" + result.toString());

            try {
                RespBean respBean = (RespBean) JsonUtils.fromJson(result.toString(), OutDataClass);
                if (null == respBean) {

                    onError(null);
                    return;
                }
                if (TextUtils.equals("401", respBean.getStatus()) || TextUtils.equals("403", respBean.getStatus())) {
                    RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();
                    loginAsyncTask.execute(respBean.getMsg());
                    return;
                }

                if (TextUtils.equals("500", respBean.getStatus())) {
                    respBean.setMsg(context.getResources().getString(R.string.network_errer));
                }

                onError(respBean);

            } catch (Exception e) {
                onError(null);
            }
        }

        @Override
        public void onCancel(Object result) {
            super.onCancel(result);

        }

        @Override
        public void onTimeout(Object result) {
            super.onTimeout(result);

        }

    };

    public abstract void enqueue();

    public abstract void parameter(ParameterBean iBean);

    public void callBack(OnCallBackSuccessListener onCallBackSuccessListener) {
        this.onCallBackSuccessListener = onCallBackSuccessListener;
    }

    public void callBack(OnCallBackSuccessListener onCallBackSuccessListener, OnCallBackFailListener onCallBackFailListener) {
        this.onCallBackSuccessListener = onCallBackSuccessListener;
        this.onCallBackFailListener = onCallBackFailListener;
    }

    public void callBack(OnCallBackFailListener onCallBackFailListener) {
        this.onCallBackFailListener = onCallBackFailListener;
    }

    protected void onCallSucceed(RespBean respBean) {
    }

    protected void onCallFail(RespBean respBean) {
    }


    public void cancel() {
        if (null != callWrap)
            callWrap.cancel();
    }


    private void onError(RespBean respBean) {

        if (null == respBean) {
            respBean = new RespBean();
            respBean.setStatus("0");
            respBean.setMsg("");
        }

        onCallFail(respBean);

        if (null != onCallBackFailListener) {

            onCallBackFailListener.onFail(respBean);
            return;
        }

        String msg = respBean.getMsg();
        if (TextUtils.isEmpty(msg)) {
            DialogUtil.showDisCoverNetToast((Activity) context);
            return;
        }
        DialogUtil.showDisCoverNetToast((Activity) context, msg);


    }


}
