package cn.chono.yopper.Service.Http.OrderEvaluation;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 评价订单
 * Created by cc on 16/5/4.
 */
public class OrderEvaluationService extends HttpService {
    public OrderEvaluationService(Context context) {
        super(context);
    }


    OrderEvaluationBean mOrderEvaluationBean;

    @Override
    public void enqueue() {

        OutDataClass = OrderEvaluationRespEntity.class;


        String url = HttpURL.order_evaluation + mOrderEvaluationBean.id + "/evaluation";

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("stars", mOrderEvaluationBean.stars);

        hashMap.put("tags", mOrderEvaluationBean.tags);

        hashMap.put("description", mOrderEvaluationBean.description);

        OKHttpUtils.post(context, url, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mOrderEvaluationBean = (OrderEvaluationBean) iBean;
    }
}
