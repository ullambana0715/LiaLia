package cn.chono.yopper.Service.Http.UserLogin;

import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class UserLoginRespBean extends RespBean {

    private LoginUser resp;

    public LoginUser getResp() {
        return resp;
    }

    public void setResp(LoginUser resp) {
        this.resp = resp;
    }
}
