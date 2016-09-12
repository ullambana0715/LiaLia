package cn.chono.yopper.Service.Http.DatingsList;

import cn.chono.yopper.data.AppointListDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingListRespBean extends RespBean {

    private AppointListDto resp;

    public AppointListDto getResp() {
        return resp;
    }

    public void setResp(AppointListDto resp) {
        this.resp = resp;
    }
}
