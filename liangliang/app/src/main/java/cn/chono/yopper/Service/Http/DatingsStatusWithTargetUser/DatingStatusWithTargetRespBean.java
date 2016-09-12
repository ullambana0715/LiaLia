package cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.DatingUserListDto;


public class DatingStatusWithTargetRespBean extends RespBean {

    private DatingInfoStateDto resp;

    public DatingInfoStateDto getResp() {
        return resp;
    }

    public void setResp(DatingInfoStateDto resp) {
        this.resp = resp;
    }
}
