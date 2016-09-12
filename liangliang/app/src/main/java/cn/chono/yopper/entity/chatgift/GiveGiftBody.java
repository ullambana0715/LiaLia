package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class GiveGiftBody implements Serializable {

    /**
     * datingId : sample string 1
     * toUserId : 2
     * isFirstCall : true
     */

    private String datingId;
    private int toUserId;
    private boolean isFirstCall;

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public boolean isIsFirstCall() {
        return isFirstCall;
    }

    public void setIsFirstCall(boolean isFirstCall) {
        this.isFirstCall = isFirstCall;
    }

    @Override
    public String toString() {
        return "GiveGiftBody{" +
                "datingId='" + datingId + '\'' +
                ", toUserId=" + toUserId +
                ", isFirstCall=" + isFirstCall +
                '}';
    }
}
