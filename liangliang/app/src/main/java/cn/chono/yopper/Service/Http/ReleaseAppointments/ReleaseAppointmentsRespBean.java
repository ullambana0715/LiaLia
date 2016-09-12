package cn.chono.yopper.Service.Http.ReleaseAppointments;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.AppointDetailDto;

/**
 * Created by zxb on 2016/1/4.
 */
public class ReleaseAppointmentsRespBean extends RespBean {
    private AppointDetailDto resp;

    public AppointDetailDto getResp() {
        return resp;
    }

    public void setResp(AppointDetailDto resp) {
        this.resp = resp;
    }
}
