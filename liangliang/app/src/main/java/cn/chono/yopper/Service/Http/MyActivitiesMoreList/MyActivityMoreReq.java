package cn.chono.yopper.Service.Http.MyActivitiesMoreList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 2016/6/13.
 */
public class MyActivityMoreReq extends ParameterBean {
    private String nextQuery;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }
}
