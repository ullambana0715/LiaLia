package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class PresentGiftInfoBean implements Serializable {
    private String giftId;
    private String datingId;
    private int fromUserId;
    private int toUserId;

    private boolean isFirstCall;
    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public boolean isFirstCall() {
        return isFirstCall;
    }

    public void setFirstCall(boolean firstCall) {
        isFirstCall = firstCall;
    }
}
