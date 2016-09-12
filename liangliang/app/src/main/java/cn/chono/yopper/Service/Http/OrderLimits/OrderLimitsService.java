package cn.chono.yopper.Service.Http.OrderLimits;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 投诉订单限制
 * Created by cc on 16/5/6.
 */
public class OrderLimitsService extends HttpService {
    public OrderLimitsService(Context context) {
        super(context);
    }

    OrderLimitsBean mOrderLimitsBean;

    @Override
    public void enqueue() {

        OutDataClass = OrderLimitsRespEntity.class;

        StringBuilder sb = new StringBuilder(HttpURL.orders_limits);

        sb.append(mOrderLimitsBean.id);

        sb.append("/limits");

        OKHttpUtils.get(context, sb.toString(), okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mOrderLimitsBean = (OrderLimitsBean) iBean;
    }
}
