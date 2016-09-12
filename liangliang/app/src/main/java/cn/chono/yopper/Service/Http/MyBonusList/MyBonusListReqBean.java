package cn.chono.yopper.Service.Http.MyBonusList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by yangjinyu on 16/3/15.
 */
public class MyBonusListReqBean extends ParameterBean{
    private int start;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
