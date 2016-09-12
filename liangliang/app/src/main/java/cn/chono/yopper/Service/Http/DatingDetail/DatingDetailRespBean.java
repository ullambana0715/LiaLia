package cn.chono.yopper.Service.Http.DatingDetail;

import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class DatingDetailRespBean extends RespBean {

    private AppointDetailDto resp;

    public AppointDetailDto getResp() {
        return resp;
    }

    public void setResp(AppointDetailDto resp) {
        this.resp = resp;
    }
}
