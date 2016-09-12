package cn.chono.yopper.Service.Http.SubmitVideoUserOnly;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/23.
 */
public class SubmitVideoUserOnlyRespBean extends RespBean {
    private VideouserOnly resp;

    public VideouserOnly getResp() {
        return resp;
    }

    public void setResp(VideouserOnly resp) {
        this.resp = resp;
    }

    public class VideouserOnly implements Serializable{
        private boolean chatWithVideoUserOnly;

        public boolean isChatWithVideoUserOnly() {
            return chatWithVideoUserOnly;
        }

        public void setChatWithVideoUserOnly(boolean chatWithVideoUserOnly) {
            this.chatWithVideoUserOnly = chatWithVideoUserOnly;
        }
    }
}
