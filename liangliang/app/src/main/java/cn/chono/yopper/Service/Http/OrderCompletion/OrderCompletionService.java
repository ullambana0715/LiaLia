package cn.chono.yopper.Service.Http.OrderCompletion;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 *
 *订单详情
 */
public class OrderCompletionService extends HttpService {
    public OrderCompletionService(Context context) {
        super(context);
    }


    private OrderCompletionBean nearbyBean;
    @Override
    public void enqueue() {

        OutDataClass=OrderCompletionRespBean.class;


        String url = HttpURL.completion_order +nearbyBean.getId()+"/completion";

        callWrap= OKHttpUtils.put(context, url, null,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearbyBean= (OrderCompletionBean) iBean;
    }
}
