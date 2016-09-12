package cn.chono.yopper.Service.Http.ActivitySignStatus;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/7/1.
 */
public class ActivitySignRespBean extends RespBean {
    private SignStatus resp;

    public SignStatus getResp() {
        return resp;
    }

    public void setResp(SignStatus resp) {
        this.resp = resp;
    }

    public class SignStatus{
        private int activityStatus;

        public int getStatus() {
            return activityStatus;
        }

        public void setStatus(int status) {
            this.activityStatus = status;
        }
    }
}
