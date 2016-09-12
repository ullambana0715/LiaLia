package cn.chono.yopper.Service.Http.VisitorsList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/23.
 */
public class VisitorsListBean extends ParameterBean {
    private int userid;
    private int start;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
