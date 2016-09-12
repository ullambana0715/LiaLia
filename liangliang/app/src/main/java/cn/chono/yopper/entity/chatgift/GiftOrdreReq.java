package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class GiftOrdreReq implements Serializable {


    /**
     * needAppleCount : 1
     * presentGiftInfo : {"giftId":"sample string 1","datingId":"sample string 2","fromUserId":3,"toUserId":4}
     */

    private int needAppleCount;
    /**
     * giftId : sample string 1
     * datingId : sample string 2
     * fromUserId : 3
     * toUserId : 4
     */

    private PresentGiftInfoBean presentGiftInfo;

    public int getNeedAppleCount() {
        return needAppleCount;
    }

    public void setNeedAppleCount(int needAppleCount) {
        this.needAppleCount = needAppleCount;
    }

    public PresentGiftInfoBean getPresentGiftInfo() {
        return presentGiftInfo;
    }

    public void setPresentGiftInfo(PresentGiftInfoBean presentGiftInfo) {
        this.presentGiftInfo = presentGiftInfo;
    }
}
