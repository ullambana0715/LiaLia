package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by sunquan on 16/5/18.
 */
public class UserPhoto implements Serializable {
    // 照片路径
    private String imageUrl;

    // 点赞数量
    private int praiseCount;

    // 点赞状态（1:点赞过 2:未点赞过）
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
        return "UserPhoto{" +
                "imageUrl='" + imageUrl + '\'' +
                ", praiseCount=" + praiseCount +
                ", praiseStatus=" + praiseStatus +
                '}';
    }
}
