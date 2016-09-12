package cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingStatusWithTargetBean extends ParameterBean {


    private String datingId;

    private int otherUserId;

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    public int getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(int otherUserId) {
        this.otherUserId = otherUserId;
    }
}
