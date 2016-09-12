package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class OrderProductBean implements Serializable {
    private String productId;
    private String productName;
    private String description;
    private int count;
    private int giveCount;
    private boolean isHighlight;
    private int price;
    private String imageUrl;
    private String applePayProductId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }

    public boolean isIsHighlight() {
        return isHighlight;
    }

    public void setIsHighlight(boolean isHighlight) {
        this.isHighlight = isHighlight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getApplePayProductId() {
        return applePayProductId;
    }

    public void setApplePayProductId(String applePayProductId) {
        this.applePayProductId = applePayProductId;
    }

    @Override
    public String toString() {
        return "OrderProductBean{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", giveCount=" + giveCount +
                ", isHighlight=" + isHighlight +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", applePayProductId='" + applePayProductId + '\'' +
                '}';
    }
}
