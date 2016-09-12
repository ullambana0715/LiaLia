package cn.chono.yopper.Service.Http.DatingRequirment;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.entity.DatingRequirementData;

/**
 * Created by zxb on 2015/11/22.
 */
public class DatingRequirmentRespBean extends RespBean {

    private DatingRequirementData resp;

    public DatingRequirementData getResp() {
        return resp;
    }

    public void setResp(DatingRequirementData resp) {
        this.resp = resp;
    }
}
