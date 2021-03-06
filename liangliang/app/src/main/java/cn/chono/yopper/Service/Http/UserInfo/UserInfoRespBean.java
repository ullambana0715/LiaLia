package cn.chono.yopper.Service.Http.UserInfo;

import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class UserInfoRespBean extends RespBean {
    private UserDto resp;

    public UserDto getResp() {
        return resp;
    }

    public void setResp(UserDto resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "UserInfoRespBean{" +
                "resp=" + resp +
                '}';
    }
}
