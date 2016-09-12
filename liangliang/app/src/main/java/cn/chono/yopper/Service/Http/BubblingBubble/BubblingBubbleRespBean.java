package cn.chono.yopper.Service.Http.BubblingBubble;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class BubblingBubbleRespBean extends RespBean {
    private  TTResp resp;

    public TTResp getResp() {
        return resp;
    }

    public void setResp(TTResp resp) {
        this.resp = resp;
    }

    public class TTResp implements Serializable{
        private String id;
        private String message;
        private boolean success;
        private int reason;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getReason() {
            return reason;
        }

        public void setReason(int reason) {
            this.reason = reason;
        }
    }


}
