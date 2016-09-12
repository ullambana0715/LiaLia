package cn.chono.yopper.Service.Http.SubmitDatingPurpose;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/23.
 */
public class SubmitDatingPurposeBean extends ParameterBean {
    private int purpose;
    private int userId;

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
