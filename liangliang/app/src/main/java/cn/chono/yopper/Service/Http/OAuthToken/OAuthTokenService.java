package cn.chono.yopper.Service.Http.OAuthToken;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.OrderCancel.OrderCancelBean;
import cn.chono.yopper.Service.Http.OrderList.OrderListRespBean;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;


public class OAuthTokenService extends HttpService {
    public OAuthTokenService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {

        OutDataClass = OAuthTokenRespBean.class;

        callWrap = OKHttpUtils.post(context, HttpURL.get_OAuthToken,null, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {

    }

}
