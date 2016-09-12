package cn.chono.yopper.Service.Http.UserInfoPFruit;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by jianghua on 2016/3/15.
 */
public class PFruitRespBean extends RespBean {

    //可用P果总数
    private String availableBalance;

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }
}
