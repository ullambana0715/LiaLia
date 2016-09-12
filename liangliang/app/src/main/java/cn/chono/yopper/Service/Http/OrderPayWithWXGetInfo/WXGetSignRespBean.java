package cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by sunquan on 16/4/28.
 */
public class WXGetSignRespBean extends RespBean {

    private WXGetSignRespDto resp;

    public WXGetSignRespDto getResp() {
        return resp;
    }

    public void setResp(WXGetSignRespDto resp) {
        this.resp = resp;
    }


}
