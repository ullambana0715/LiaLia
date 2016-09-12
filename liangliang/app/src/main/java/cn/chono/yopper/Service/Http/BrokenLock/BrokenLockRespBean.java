package cn.chono.yopper.Service.Http.BrokenLock;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.BubblingList;

/**
 * Created by zxb on 2015/11/21.
 */
public class BrokenLockRespBean extends RespBean {
    private UnLockRespDto resp;

    public UnLockRespDto getResp() {
        return resp;
    }

    public void setResp(UnLockRespDto resp) {
        this.resp = resp;
    }
}
