package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/3/18.
 */
public class HoYo implements Serializable {

    // 用户Id
    private String userId;

    // 昵称
    private String name;

    // 对外头像
    private String headImg;

    // 赠送苹果数
    private Integer transferedCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getTransferedCount() {
        return transferedCount;
    }

    public void setTransferedCount(Integer transferedCount) {
        this.transferedCount = transferedCount;
    }

    @Override
    public String toString() {
        return "HoYo{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", headImg='" + headImg + '\'' +
                ", transferedCount=" + transferedCount +
                '}';
    }
}
