package cn.chono.yopper.Service.Http.OrderCounsel;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/5/4.
 */
public class OrderCounselBean extends ParameterBean {

    public int counselType;// 咨询类型（0：塔罗 1：星盘）

    public int receiveUserId;// 接单用户Id

    public String bookingTime;// 预约时间

}
