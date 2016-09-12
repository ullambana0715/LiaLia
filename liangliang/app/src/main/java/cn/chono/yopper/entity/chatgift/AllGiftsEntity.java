package cn.chono.yopper.entity.chatgift;

import java.util.List;

/**
 * Created by sunquan on 16/8/4.
 */
public class AllGiftsEntity {

    private int remainAppleCount;

    private List<GiftInfoEntity> giftsOfUnpassHot;

    private List<GiftInfoEntity> giftsOfPassHot;

    public int getRemainAppleCount() {
        return remainAppleCount;
    }

    public void setRemainAppleCount(int remainAppleCount) {
        this.remainAppleCount = remainAppleCount;
    }

    public List<GiftInfoEntity> getGiftsOfUnpassHot() {
        return giftsOfUnpassHot;
    }

    public void setGiftsOfUnpassHot(List<GiftInfoEntity> giftsOfUnpassHot) {
        this.giftsOfUnpassHot = giftsOfUnpassHot;
    }

    public List<GiftInfoEntity> getGiftsOfPassHot() {
        return giftsOfPassHot;
    }

    public void setGiftsOfPassHot(List<GiftInfoEntity> giftsOfPassHot) {
        this.giftsOfPassHot = giftsOfPassHot;
    }
}
