package cn.chono.yopper.Service.Http.ProfileUser;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class ProfileUserRespBean extends RespBean {

    private  boolean resp;

    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
    }
}
