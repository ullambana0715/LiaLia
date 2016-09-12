package cn.chono.yopper.Service.Http.ActivitySignStatus;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by yangjinyu on 16/7/1.
 */
public class ActivitySignReqBean extends ParameterBean {
    private String activityId;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
