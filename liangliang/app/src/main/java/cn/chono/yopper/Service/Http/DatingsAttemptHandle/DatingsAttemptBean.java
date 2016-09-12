package cn.chono.yopper.Service.Http.DatingsAttemptHandle;

import cn.chono.yopper.Service.Http.ParameterBean;


public class DatingsAttemptBean extends ParameterBean {

    private String targetUserId;

    private String datingId;

    private int type;

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
