package cn.chono.yopper.Service.Http.KeyStatus;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class KeyStatusBean extends ParameterBean {


    private String TargetUserId;

    public String getTargetUserId() {
        return TargetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        TargetUserId = targetUserId;
    }
}
