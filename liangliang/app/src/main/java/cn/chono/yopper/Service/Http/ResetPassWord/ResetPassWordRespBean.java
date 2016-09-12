package cn.chono.yopper.Service.Http.ResetPassWord;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class ResetPassWordRespBean extends RespBean {
    private boolean resp;

    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
    }
}
