package cn.chono.yopper.Service.Http.VedioDetail;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/23.
 */
public class VedioDetailBean extends ParameterBean {
    private int userId;
    private int VisitorId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVisitorId() {
        return VisitorId;
    }

    public void setVisitorId(int visitorId) {
        VisitorId = visitorId;
    }
}
