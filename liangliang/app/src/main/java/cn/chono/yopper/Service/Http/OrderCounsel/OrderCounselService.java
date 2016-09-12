package cn.chono.yopper.Service.Http.OrderCounsel;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 咨询服务下单（预约塔罗师）
 * Created by cc on 16/5/4.
 */
public class OrderCounselService extends HttpService {
    public OrderCounselService(Context context) {
        super(context);
    }


    OrderCounselBean mOrderCounselBean;

    @Override
    public void enqueue() {

        OutDataClass = OrderCounselRespEntity.class;

        HashMap<String, Object> hashMap = new HashMap<>();


        hashMap.put("counselType", mOrderCounselBean.counselType);
        hashMap.put("receiveUserId", mOrderCounselBean.receiveUserId);
        hashMap.put("bookingTime", mOrderCounselBean.bookingTime);

        OKHttpUtils.post(context, HttpURL.order_counsel, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mOrderCounselBean = (OrderCounselBean) iBean;
    }
}
