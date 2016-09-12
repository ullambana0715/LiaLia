package cn.chono.yopper.Service.Http.OrderDetail;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 *
 *订单详情
 */
public class OrderDetailService extends HttpService {
    public OrderDetailService(Context context) {
        super(context);
    }


    private OrderDetailBean nearbyBean;
    @Override
    public void enqueue() {

        OutDataClass=OrderDetailRespBean.class;

        String url = HttpURL.order_detail +nearbyBean.getId();

        callWrap= OKHttpUtils.get(context, url,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearbyBean= (OrderDetailBean) iBean;
    }
}
