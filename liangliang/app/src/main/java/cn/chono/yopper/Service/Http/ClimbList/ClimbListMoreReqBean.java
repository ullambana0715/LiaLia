package cn.chono.yopper.Service.Http.ClimbList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 16/3/29.
 */
public class ClimbListMoreReqBean extends ParameterBean {
    private String nextQuery;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

}
