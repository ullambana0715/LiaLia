package cn.chono.yopper.Service.Http.MyBonusList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 16/3/24.
 */
public class MyBonusListMoreReqBean extends ParameterBean{
    private String nextQuery;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String start) {
        this.nextQuery = start;
    }
}
