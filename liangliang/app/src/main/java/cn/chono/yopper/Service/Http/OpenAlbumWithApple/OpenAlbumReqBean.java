package cn.chono.yopper.Service.Http.OpenAlbumWithApple;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by yangjinyu on 16/7/26.
 */
public class OpenAlbumReqBean extends ParameterBean {
    private String userId;
    private String lookedUserId;//被查看人的id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLookedUserId() {
        return lookedUserId;
    }

    public void setLookedUserId(String lookedUserId) {
        this.lookedUserId = lookedUserId;
    }
}
