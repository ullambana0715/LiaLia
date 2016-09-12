package cn.chono.yopper.Service.Http.SignUpActivity;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by yangjinyu on 16/6/15.
 */
public class SighUpRespBean extends RespBean {
    private SignUpBack resp;

    public SignUpBack getResp() {
        return resp;
    }

    public void setResp(SignUpBack resp) {
        this.resp = resp;
    }

    public class SignUpBack{
        private int result;
        private String msg;
        private String orderId;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
