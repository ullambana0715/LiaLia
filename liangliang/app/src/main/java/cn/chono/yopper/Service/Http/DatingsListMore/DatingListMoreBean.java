package cn.chono.yopper.Service.Http.DatingsListMore;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingListMoreBean extends ParameterBean {
    private String nextQuery;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }
}
