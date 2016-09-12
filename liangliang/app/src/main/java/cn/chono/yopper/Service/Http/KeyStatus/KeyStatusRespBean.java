package cn.chono.yopper.Service.Http.KeyStatus;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class KeyStatusRespBean extends RespBean {
    private KeyStatusDto resp;

    public KeyStatusDto getResp() {
        return resp;
    }

    public void setResp(KeyStatusDto resp) {
        this.resp = resp;
    }
}
