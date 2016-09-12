package cn.chono.yopper.Service.Http.SubmitDatingPurpose;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/23.
 */
public class SubmitDatingPurposeRespBean extends RespBean {
    private Purpose resp;

    public Purpose getResp() {
        return resp;
    }

    public void setResp(Purpose resp) {
        this.resp = resp;
    }

    public class Purpose implements Serializable {
        private int purpose;

        public int getPurpose() {
            return purpose;
        }

        public void setPurpose(int purpose) {
            this.purpose = purpose;
        }
    }
}
