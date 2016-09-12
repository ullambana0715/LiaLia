package cn.chono.yopper.Service.Http.OrderCounsel;

/**
 * Created by cc on 16/5/4.
 */
public class OrderCounselEntity {

    public int result;// 预约结果（0：成功 1：库存不足 2：存在该咨询师其他订单 3：预约时间过期 4：预约时间已删除 5：不能预约30分钟内的订单 6：服务被暂停）

    public String msg;// 失败信息

    public OrderEntity order;// 订单基本信息

}
