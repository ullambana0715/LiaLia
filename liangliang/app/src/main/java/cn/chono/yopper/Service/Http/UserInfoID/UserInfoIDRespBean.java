package cn.chono.yopper.Service.Http.UserInfoID;

import cn.chono.yopper.data.BaseUser;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class UserInfoIDRespBean  extends RespBean{

    private BaseUser resp;

    public BaseUser getResp() {
        return resp;
    }

    public void setResp(BaseUser resp) {
        this.resp = resp;
    }
}
