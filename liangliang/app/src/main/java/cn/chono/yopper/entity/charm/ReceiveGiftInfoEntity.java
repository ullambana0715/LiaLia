package cn.chono.yopper.entity.charm;

import cn.chono.yopper.entity.chatgift.GiftGiverEntity;
import cn.chono.yopper.entity.chatgift.GiftInfoEntity;

/**
 * Created by sunquan on 16/8/5.
 */
public class ReceiveGiftInfoEntity {

    private GiftGiverEntity giver;

    private GiftInfoEntity gift;

    // 增加的魅力值
    private int increasedCharm;

    // 签收时间
    private String signForTime;

    public GiftGiverEntity getGiver() {
        return giver;
    }

    public void setGiver(GiftGiverEntity giver) {
        this.giver = giver;
    }

    public GiftInfoEntity getGift() {
        return gift;
    }

    public void setGift(GiftInfoEntity gift) {
        this.gift = gift;
    }

    public int getIncreasedCharm() {
        return increasedCharm;
    }

    public void setIncreasedCharm(int increasedCharm) {
        this.increasedCharm = increasedCharm;
    }

    public String getSignForTime() {
        return signForTime;
    }

    public void setSignForTime(String signForTime) {
        this.signForTime = signForTime;
    }
}
