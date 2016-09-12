package cn.chono.yopper.Service.Http.LikeHandle;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class LikeHandleBean extends ParameterBean {

    private boolean islike;
    private  int userId;

    public boolean islike() {
        return islike;
    }

    public void setIslike(boolean islike) {
        this.islike = islike;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
