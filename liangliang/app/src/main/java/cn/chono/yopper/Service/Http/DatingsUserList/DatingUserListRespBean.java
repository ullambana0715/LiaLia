package cn.chono.yopper.Service.Http.DatingsUserList;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.AppointListDto;
import cn.chono.yopper.data.DatingUserListDto;


public class DatingUserListRespBean extends RespBean {

    private DatingUserListDto resp;

    public DatingUserListDto getResp() {
        return resp;
    }

    public void setResp(DatingUserListDto resp) {
        this.resp = resp;
    }
}
