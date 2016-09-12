package cn.chono.yopper.Service.Http.OrderLastest;

/**
 * Created by cc on 16/5/20.
 */
public class OrderLastestEntity {

    private String orderId;// 订单Id

    private int bookingUserId;

    private int receiveUserId;

    private int orderType;

    private String orderNo;

    private long totalFee; // 总价格（单位：分）

    private int counselType;// 咨询类型（0：塔罗 1：星盘）

    private String bookingTime;// 预约时间

    private int orderStatus; // 订单状态（0：待付款 1：预约成功 2：已取消 3：咨询完成-未评价 4：咨询完成-已评价） 5：待确认完成）

    private int orderStatusForChat;

    private String createTime;// 创建时间

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getBookingUserId() {
        return bookingUserId;
    }

    public void setBookingUserId(int bookingUserId) {
        this.bookingUserId = bookingUserId;
    }

    public int getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(int receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(long totalFee) {
        this.totalFee = totalFee;
    }

    public int getCounselType() {
        return counselType;
    }

    public void setCounselType(int counselType) {
        this.counselType = counselType;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getOrderStatusForChat() {
        return orderStatusForChat;
    }

    public void setOrderStatusForChat(int orderStatusForChat) {
        this.orderStatusForChat = orderStatusForChat;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
