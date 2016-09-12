package cn.chono.yopper.Service.Http.OrderEvaluation;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/5/4.
 */
public class OrderEvaluationBean extends ParameterBean {

    public String  id;//订单Id

    public int stars;//星级

    public String[] tags;

    public String description;
}
