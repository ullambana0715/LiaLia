package cn.chono.yopper.Service.Http.UserSync;

import cn.chono.yopper.data.SyncDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class UserSyncRespBean extends RespBean {

    private SyncDto resp;

    public SyncDto getResp() {
        return resp;
    }

    public void setResp(SyncDto resp) {
        this.resp = resp;
    }
}
