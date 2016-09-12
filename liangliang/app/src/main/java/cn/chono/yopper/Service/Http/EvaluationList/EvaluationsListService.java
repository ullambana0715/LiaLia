package cn.chono.yopper.Service.Http.EvaluationList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationsListService extends HttpService {
    public EvaluationsListService(Context context) {
        super(context);
    }

    EvaluationListBean mEvaluationListBean;

    @Override
    public void enqueue() {

        OutDataClass = EvaluationListRespEntity.class;


        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("ReceiveUserId", mEvaluationListBean.receiveUserId);
        hashMap.put("Start", mEvaluationListBean.start);
        hashMap.put("Rows", mEvaluationListBean.rows);

        OKHttpUtils.get(context, HttpURL.evaluation, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mEvaluationListBean = (EvaluationListBean) iBean;
    }
}
