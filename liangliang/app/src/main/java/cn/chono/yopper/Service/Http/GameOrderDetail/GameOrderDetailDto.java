package cn.chono.yopper.Service.Http.GameOrderDetail;

/**
 * Created by sunquan on 16/4/28.
 */
public class GameOrderDetailDto {

    //订单ID
    private String orderId;

    private int orderType;

    //订单编号
    private String orderNo;

    //咨询费用

    private long totalFee;

    // 商品名称
    private String productName;

    //预约时间
    private String bookingTime;

    private String applePayProductId;

    private String productDescription;


    // 创建时间
    private String createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getApplePayProductId() {
        return applePayProductId;
    }

    public void setApplePayProductId(String applePayProductId) {
        this.applePayProductId = applePayProductId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
