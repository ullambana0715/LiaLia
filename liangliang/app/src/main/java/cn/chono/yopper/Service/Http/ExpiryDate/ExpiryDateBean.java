package cn.chono.yopper.Service.Http.ExpiryDate;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 2016/3/15.
 */
public class ExpiryDateBean extends ParameterBean {
    private String prizeId;//奖品Id

    private UserInfoBean userAddress;//用户地址信息

    public UserInfoBean getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserInfoBean userAddress) {
        this.userAddress = userAddress;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

}
