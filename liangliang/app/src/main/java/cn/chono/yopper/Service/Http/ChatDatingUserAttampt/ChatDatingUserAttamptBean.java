package cn.chono.yopper.Service.Http.ChatDatingUserAttampt;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/23.
 */
public class ChatDatingUserAttamptBean extends ParameterBean {

    private int targetUserId;


    private String datingId;


    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }


    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }


}
