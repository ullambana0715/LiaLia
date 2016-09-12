package cn.chono.yopper.entity.likeBean;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/7/25.
 */
public class CancelIlike implements Serializable {

    /**
     * userLikeState : 0
     * likeResult : 0
     * message : sample string 1
     */

    private int userLikeState;
    private int likeResult;
    private String message;

    public int getUserLikeState() {
        return userLikeState;
    }

    public void setUserLikeState(int userLikeState) {
        this.userLikeState = userLikeState;
    }

    public int getLikeResult() {
        return likeResult;
    }

    public void setLikeResult(int likeResult) {
        this.likeResult = likeResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
