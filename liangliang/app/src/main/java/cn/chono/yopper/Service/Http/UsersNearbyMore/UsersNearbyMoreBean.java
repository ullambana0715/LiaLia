package cn.chono.yopper.Service.Http.UsersNearbyMore;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class UsersNearbyMoreBean extends ParameterBean {

    private String nextQuery;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }
}
