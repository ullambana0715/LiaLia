package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/1.
 */
public class GiftInfoEntity implements Serializable {
    private String giftId;
    private String giftName;
    private String imageUrl;
    private int appleCount;
    private int charm;

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAppleCount() {
        return appleCount;
    }

    public void setAppleCount(int appleCount) {
        this.appleCount = appleCount;
    }

    public int getCharm() {
        return charm;
    }

    public void setCharm(int charm) {
        this.charm = charm;
    }

    @Override
    public String toString() {
        return "GiftInfoEntity{" +
                "giftId='" + giftId + '\'' +
                ", giftName='" + giftName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", appleCount=" + appleCount +
                ", charm=" + charm +
                '}';
    }
}
