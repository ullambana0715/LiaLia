package cn.chono.yopper.Service.Http.VerifiCation;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/19.
 */
public class VerifiCationRespBean extends RespBean {
    private VerifyCode resp;

    public VerifyCode getResp() {
        return resp;
    }

    public void setResp(VerifyCode resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "VerifyRespBean{" +
                "resp=" + resp +
                '}';
    }

    public class VerifyCode implements Serializable {
        private boolean available;

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }


}
