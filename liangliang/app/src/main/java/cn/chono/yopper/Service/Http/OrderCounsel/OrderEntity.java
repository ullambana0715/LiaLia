package cn.chono.yopper.Service.Http.OrderCounsel;

/**
 * Created by cc on 16/5/4.
 */
public class OrderEntity {

    public UserEntity user;// 咨询师信息

    public String orderId;  // 订单Id

    public double totalFee;// 总价格（单位：分）

    public int counselType; // 咨询类型（0：塔罗 1：星盘）

    public String bookingTime;// 预约时间
}
