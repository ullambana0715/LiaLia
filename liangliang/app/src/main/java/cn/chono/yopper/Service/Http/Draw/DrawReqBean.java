package cn.chono.yopper.Service.Http.Draw;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by yangjinyu on 16/3/24.
 */
public class DrawReqBean extends ParameterBean{
    private String userPrizeId;

    public String getUserPrizeId() {
        return userPrizeId;
    }

    public void setUserPrizeId(String userPrizeId) {
        this.userPrizeId = userPrizeId;
    }
}
