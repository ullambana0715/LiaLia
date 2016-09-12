package cn.chono.yopper.Service.Http.EvaluationList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationListBean extends ParameterBean {

    public int receiveUserId;//接单用户Id

    public int start;//结果集偏移量，从0开始计算；默认为0

    public int rows=20;//从偏移量start开始，返回多少条数据；默认为20

}
