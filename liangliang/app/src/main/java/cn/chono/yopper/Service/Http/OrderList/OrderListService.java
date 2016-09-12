package cn.chono.yopper.Service.Http.OrderList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.DatingsList.DatingListRespBean;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailBean;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/22.
 */
public class OrderListService extends HttpService {
    public OrderListService(Context context) {
        super(context);
    }

    private OrderListBean nearestsBean;


    @Override
    public void enqueue() {

        OutDataClass = OrderListRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("OrderType", nearestsBean.getOrderType());
        hashMap.put("Start", 0);

        callWrap = OKHttpUtils.get(context, HttpURL.my_order_list,hashMap, okHttpListener);
    }


    @Override
    public void parameter(ParameterBean iBean) {
        nearestsBean= (OrderListBean) iBean;
    }

}
