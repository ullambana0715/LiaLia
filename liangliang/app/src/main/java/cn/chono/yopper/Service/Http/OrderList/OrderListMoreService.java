package cn.chono.yopper.Service.Http.OrderList;

import android.content.Context;

import cn.chono.yopper.Service.Http.DatingsList.DatingListRespBean;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailBean;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/22.
 */
public class OrderListMoreService extends HttpService {

    public OrderListMoreService(Context context) {
        super(context);
    }

    private OrderListMoreBean nearestsBean;

    @Override
    public void enqueue() {

        OutDataClass = OrderListRespBean.class;

        String url = nearestsBean.getNextQuery();
        callWrap = OKHttpUtils.get(context, url, okHttpListener);
    }


    @Override
    public void parameter(ParameterBean iBean) {
        nearestsBean= (OrderListMoreBean) iBean;
    }

}
