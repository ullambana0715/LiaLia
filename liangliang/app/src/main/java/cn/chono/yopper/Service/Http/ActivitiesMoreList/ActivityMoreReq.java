package cn.chono.yopper.Service.Http.ActivitiesMoreList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 2016/6/14.
 */
public class ActivityMoreReq extends ParameterBean {
    private String nextQuery;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nexeQuery) {
        this.nextQuery = nexeQuery;
    }
}
