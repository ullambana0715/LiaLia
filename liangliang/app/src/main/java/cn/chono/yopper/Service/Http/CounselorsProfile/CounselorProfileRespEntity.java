package cn.chono.yopper.Service.Http.CounselorsProfile;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/5/3.
 */
public class CounselorProfileRespEntity extends RespBean {

    public CounselorProfileEntity resp;

    public CounselorProfileEntity getResp() {
        return resp;
    }

    public void setResp(CounselorProfileEntity resp) {
        this.resp = resp;
    }
}
