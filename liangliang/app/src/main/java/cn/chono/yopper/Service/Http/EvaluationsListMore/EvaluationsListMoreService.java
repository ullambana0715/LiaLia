package cn.chono.yopper.Service.Http.EvaluationsListMore;

import android.content.Context;

import cn.chono.yopper.Service.Http.EvaluationList.EvaluationListRespEntity;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationsListMoreService extends HttpService {
    public EvaluationsListMoreService(Context context) {
        super(context);
    }

    EvaluationsListMoreBean mEvaluationsListMoreBean;

    @Override
    public void enqueue() {

        OutDataClass = EvaluationListRespEntity.class;

        OKHttpUtils.get(context, mEvaluationsListMoreBean.nextQuery, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

        mEvaluationsListMoreBean = (EvaluationsListMoreBean) iBean;

    }
}
