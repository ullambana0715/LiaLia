package cn.chono.yopper.Service.Http.PublishWish;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class PublishWishRespBean extends RespBean {
    private  boolean resp;

    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
    }
}
