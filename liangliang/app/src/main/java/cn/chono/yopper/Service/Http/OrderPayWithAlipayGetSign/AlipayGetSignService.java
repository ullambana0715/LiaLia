package cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 *
 * 支付宝支付前置条件以及获取签名
 */
public class AlipayGetSignService extends HttpService {
    public AlipayGetSignService(Context context) {
        super(context);
    }


    private AlipayGetSignBean nearbyBean;
    @Override
    public void enqueue() {

        OutDataClass=AlipayGetSignRespBean.class;


        String url = HttpURL.alipay_get_sign +nearbyBean.getOut_trade_no()+ "/getSign";

        callWrap= OKHttpUtils.get(context, url, null, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearbyBean= (AlipayGetSignBean) iBean;
    }
}
