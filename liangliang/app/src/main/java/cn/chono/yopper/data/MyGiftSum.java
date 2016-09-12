package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by cc on 16/7/25.
 */
public class MyGiftSum implements Serializable {


    /**
     * giftId : sample string 1
     * giftName : sample string 2
     * imageUrl : sample string 3
     * appleCount : 4
     * charm : 5
     * passHot : true
     */

    private GiftBean gift;// 礼物
    /**
     * gift : {"giftId":"sample string 1","giftName":"sample string 2","imageUrl":"sample string 3","appleCount":4,"charm":5,"passHot":true}
     * sum : 1
     */

    private int sum; // 数量

    public GiftBean getGift() {
        return gift;
    }

    public void setGift(GiftBean gift) {
        this.gift = gift;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public static class GiftBean {
        private String giftId; // 礼物Id
        private String giftName;// 礼物名称
        private String imageUrl;// 礼物图片Url
        private int appleCount;// 苹果数
        private int charm;// 魅力值
        private boolean passHot;// 穿越Hot

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

        public boolean isPassHot() {
            return passHot;
        }

        public void setPassHot(boolean passHot) {
            this.passHot = passHot;
        }
    }
}
