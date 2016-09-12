package cn.chono.yopper.Service.Http.OrderFeedback;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 投诉订单
 * Created by cc on 16/5/5.
 */
public class OrderFeedbackService extends HttpService {
    public OrderFeedbackService(Context context) {
        super(context);
    }


    OrderFeedbackBean mOrderFeedbackBean;

    @Override
    public void enqueue() {

        OutDataClass = OrderFeedbackRespEntity.class;

        String url = HttpURL.order_feedback + mOrderFeedbackBean.id + "/feedback";

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("feedbackType", mOrderFeedbackBean.feedbackType);
        hashMap.put("description", mOrderFeedbackBean.description);
        hashMap.put("imageUrls", mOrderFeedbackBean.imageUrls);

        OKHttpUtils.post(context, url, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mOrderFeedbackBean = (OrderFeedbackBean) iBean;
    }
}
