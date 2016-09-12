package cn.chono.yopper.Service.Http.SignUpActivity;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by yangjinyu on 16/6/15.
 */
public class SignUpReqBean extends ParameterBean{
    private String id;
    private boolean useFee;

    public boolean isUseFee() {
        return useFee;
    }

    public void setUseFee(boolean useFee) {
        this.useFee = useFee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
