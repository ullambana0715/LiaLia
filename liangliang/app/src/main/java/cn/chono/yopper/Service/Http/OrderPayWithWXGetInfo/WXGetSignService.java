package cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.wxapi.WeixinUtils;

/**
 * 微信支付前置条件以及获取签名
 */
public class WXGetSignService extends HttpService {
    public WXGetSignService(Context context) {
        super(context);
    }


    private WXGetSignBean nearbyBean;

    @Override
    public void enqueue() {

        OutDataClass = WXGetSignRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("orderId", nearbyBean.getOrderid());


        hashMap.put("appId", WeixinUtils.APP_ID);


        hashMap.put("merchantId", WeixinUtils.MCH_ID);



        String url = HttpURL.wx_pay_get_sign;

        callWrap = OKHttpUtils.post(context, url, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearbyBean = (WXGetSignBean) iBean;
    }
}
