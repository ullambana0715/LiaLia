package cn.chono.yopper.entity.likeBean;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/3.
 */
public class PrivateAlbumBody implements Serializable{

    private int userId;
    private int lookedUserId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLookedUserId() {
        return lookedUserId;
    }

    public void setLookedUserId(int lookedUserId) {
        this.lookedUserId = lookedUserId;
    }
}
