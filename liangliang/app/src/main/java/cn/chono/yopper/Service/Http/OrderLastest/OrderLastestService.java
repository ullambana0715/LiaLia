package cn.chono.yopper.Service.Http.OrderLastest;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 根据用户Id和咨询师Id获取最近一条有效订单
 * Created by cc on 16/5/20.
 */
public class OrderLastestService extends HttpService {
    public OrderLastestService(Context context) {
        super(context);
    }

    OrderLastestBean mOrderLastestBean;

    @Override
    public void enqueue() {

        OutDataClass = OrderLastestRespEntity.class;


        String url=HttpURL.order_lastest+"bookingUserId="+mOrderLastestBean.bookingUserId
                +"&receiveUserId="+mOrderLastestBean.receiveUserId;

        OKHttpUtils.get(context, url, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        mOrderLastestBean = (OrderLastestBean) iBean;
    }
}
