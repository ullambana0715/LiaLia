package cn.chono.yopper.Service.Http.ActivityInfo;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/6/15.
 */
public class ActivityRespBean extends RespBean {
    private ActivityInfo resp;

    public ActivityInfo getResp() {
        return resp;
    }

    public void setResp(ActivityInfo resp) {
        this.resp = resp;
    }

    public class ActivityInfo{
        private String activityId;
        private int userId;
        private int joinStatus;// 报名状态（0：未报名 1：报名成功 2：报名已退款）

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getJoinStatus() {
            return joinStatus;
        }

        public void setJoinStatus(int joinStatus) {
            this.joinStatus = joinStatus;
        }
    }
}
