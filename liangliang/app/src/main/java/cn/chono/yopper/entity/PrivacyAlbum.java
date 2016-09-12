package cn.chono.yopper.entity;

import java.io.Serializable;

/**
 * Created by cc on 16/8/4.
 */
public class PrivacyAlbum implements Serializable {


    /**
     * imageUrl : sample string 1
     * praiseCount : 2
     * praiseStatus : 0
     */

    private String imageUrl;
    private int praiseCount;
    private int praiseStatus;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(int praiseStatus) {
        this.praiseStatus = praiseStatus;
    }

    @Override
    public String toString() {
        return "PrivacyAlbum{" +
                "imageUrl='" + imageUrl + '\'' +
                ", praiseCount=" + praiseCount +
                ", praiseStatus=" + praiseStatus +
                '}';
    }
}
