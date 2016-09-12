package cn.chono.yopper.Service.Http.DatingInvite;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.entity.DatingsInviteEntity;

/**
 * Created by cc on 16/3/31.
 */
public class DatingsInviteRespBean extends RespBean {

    private DatingsInviteEntity resp;


    public DatingsInviteEntity getResp() {
        return resp;
    }

    public void setResp(DatingsInviteEntity resp) {
        this.resp = resp;
    }
}
