package cn.chono.yopper.entity;

/**
 * Created by sunquan on 16/8/4.
 */
public class WithDrawItemEntity {

    // 使用的魅力值
    private int usedCharm;

    // 金额（单位：分
    private int cash;

    // 兑换时间
    private String exchangeTime;

    public int getUsedCharm() {
        return usedCharm;
    }

    public void setUsedCharm(int usedCharm) {
        this.usedCharm = usedCharm;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    @Override
    public String toString() {
        return "WithDrawItemEntity{" +
                "usedCharm=" + usedCharm +
                ", cash=" + cash +
                ", exchangeTime='" + exchangeTime + '\'' +
                '}';
    }
}
