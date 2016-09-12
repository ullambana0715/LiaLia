package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class OrderBean implements Serializable {

    private OrderProductBean product;
    private String orderId;
    private int orderType;
    private String orderNo;
    private int totalFee;
    private String productName;
    private String productDescription;
    private String createTime;

    public OrderProductBean getProduct() {
        return product;
    }

    public void setProduct(OrderProductBean product) {
        this.product = product;
    }

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "product=" + product +
                ", orderId='" + orderId + '\'' +
                ", orderType=" + orderType +
                ", orderNo='" + orderNo + '\'' +
                ", totalFee=" + totalFee +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
