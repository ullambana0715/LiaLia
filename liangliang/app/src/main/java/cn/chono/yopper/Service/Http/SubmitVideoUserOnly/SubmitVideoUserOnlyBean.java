package cn.chono.yopper.Service.Http.SubmitVideoUserOnly;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/23.
 */
public class SubmitVideoUserOnlyBean extends ParameterBean {
    private  int userId;
    private boolean chatWithVideoUserOnly;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isChatWithVideoUserOnly() {
        return chatWithVideoUserOnly;
    }

    public void setChatWithVideoUserOnly(boolean chatWithVideoUserOnly) {
        this.chatWithVideoUserOnly = chatWithVideoUserOnly;
    }
}
