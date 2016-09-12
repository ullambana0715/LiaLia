package cn.chono.yopper.Service.Http.OrderDetail;

/**
 * Created by sunquan on 16/4/28.
 */
public class OrderDetailDto {

    //订单ID
    private String orderId;

    private int orderType;

    //订单编号
    private String orderNo;

    //咨询费用

    private long totalFee;

    //咨询类型（0：塔罗 1：星盘）

    private int counselType;

    //预约时间
    private String bookingTime;

    // 订单状态（0：待付款 1：预约成功 2：已取消 3：咨询完成-未评价 4：咨询完成-已评价 5：待确认完成）
    private int orderStatus;

    // 商品名称
    private String goodsName;
    // 商品描述
    private String goodsDescription;
    // 创建时间
    private String createTime;

    // 剩余付款时间（单位：秒）
    private long remainPaymentSeconds;

    // 投诉反馈状态（0：无投诉 1：已投诉 2：已处理）
    private int feedbackStatus;

    // 投诉反馈处理结果
    private String feedbackResult;

    // 咨询师或用户信息
    private OrderUser user;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getRemainPaymentSeconds() {
        return remainPaymentSeconds;
    }

    public void setRemainPaymentSeconds(long remainPaymentSeconds) {
        this.remainPaymentSeconds = remainPaymentSeconds;
    }

    public OrderUser getUser() {
        return user;
    }

    public void setUser(OrderUser user) {
        this.user = user;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }


    public String getFeedbackResult() {
        return feedbackResult;
    }

    public void setFeedbackResult(String feedbackResult) {
        this.feedbackResult = feedbackResult;
    }

    public int getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(int feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public class OrderUser {

        private int userId;
        private String name;
        private String headImg;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }
}
