package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by SQ on 2015/12/7.
 */
public class UserFaceRatingDto implements Serializable {
    private int level;
    private String rateTime;

    public String getRateTime() {
        return rateTime;
    }

    public void setRateTime(String rateTime) {
        this.rateTime = rateTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
