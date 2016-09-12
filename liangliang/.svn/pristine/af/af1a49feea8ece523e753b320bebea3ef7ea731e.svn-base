package cn.chono.yopper.Service.Http.BubblingBubbleComments;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubbleCommentsBean extends ParameterBean {

    private String id;
    private String comment;

    private ToUserID toUserID;

    public ToUserID getToUserID() {
        return toUserID;
    }

    public void setToUserID(ToUserID toUserID) {
        this.toUserID = toUserID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class ToUserID implements Serializable{
        private int  toUserId;

        public int getToUserId() {
            return toUserId;
        }

        public void setToUserId(int toUserId) {
            this.toUserId = toUserId;
        }
    }
}
