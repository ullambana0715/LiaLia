package cn.chono.yopper.Service.Http.BubbleInfo;

import cn.chono.yopper.data.BubblingList;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class BubbleInfoRespBean extends RespBean {
    private BubblingList resp;

    public BubblingList getResp() {
        return resp;
    }

    public void setResp(BubblingList resp) {
        this.resp = resp;
    }
}
