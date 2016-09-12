package cn.chono.yopper.Service.Http.OrderCancel;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 取消订单
 */
public class OrderCancelService extends HttpService {
    public OrderCancelService(Context context) {
        super(context);
    }

    private OrderCancelBean nearestsBean;


    @Override
    public void enqueue() {

        OutDataClass = OrderCancelRespEntity.class;

        callWrap = OKHttpUtils.put(context, HttpURL.order_cancel+nearestsBean.id+"/cancel",null, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearestsBean= (OrderCancelBean) iBean;
    }

}
