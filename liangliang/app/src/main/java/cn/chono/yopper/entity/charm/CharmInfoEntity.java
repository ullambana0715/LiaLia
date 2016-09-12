package cn.chono.yopper.entity.charm;

/**
 * Created by yangjinyu on 16/7/27.
 */
public class CharmInfoEntity {

    // 剩余魅力值
    private int remainCharm;

    // 剩余魅力值可兑换金钱（单位：分）
    private int remainCharmFee;

    // 魅力值单价（单位：分）
    private int charmPrice;

    private ReceiveGiftListEntity myGifts;


    public int getRemainCharm() {
        return remainCharm;
    }

    public void setRemainCharm(int remainCharm) {
        this.remainCharm = remainCharm;
    }

    public int getRemainCharmFee() {
        return remainCharmFee;
    }

    public void setRemainCharmFee(int remainCharmFee) {
        this.remainCharmFee = remainCharmFee;
    }

    public int getCharmPrice() {
        return charmPrice;
    }

    public void setCharmPrice(int charmPrice) {
        this.charmPrice = charmPrice;
    }

    public ReceiveGiftListEntity getMyGifts() {
        return myGifts;
    }

    public void setMyGifts(ReceiveGiftListEntity myGifts) {
        this.myGifts = myGifts;
    }
}
