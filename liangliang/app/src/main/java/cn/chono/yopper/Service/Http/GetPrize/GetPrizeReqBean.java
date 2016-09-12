package cn.chono.yopper.Service.Http.GetPrize;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.ExpiryDate.UserInfoBean;
import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by yangjinyu on 16/3/21.
 */
public class GetPrizeReqBean extends ParameterBean {

    private String userPrizeId;//奖品Id

    private UserInfoBean userAddress;//用户地址信息

    public UserInfoBean getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserInfoBean userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPrizeId() {
        return userPrizeId;
    }

    public void setUserPrizeId(String prizeId) {
        this.userPrizeId = prizeId;
    }
}
