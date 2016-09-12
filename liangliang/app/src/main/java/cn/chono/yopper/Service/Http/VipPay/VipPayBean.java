package cn.chono.yopper.Service.Http.VipPay;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/6/14.
 */
public class VipPayBean extends ParameterBean {

    public int userId;


    public int userPosition;//V2.5.4 vip身份枚举 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP


}
