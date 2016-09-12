package cn.chono.yopper.Service.Http.DattingAttampt;

import cn.chono.yopper.entity.AttamptRespDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class DattingAttamptRespBean extends RespBean{

    private AttamptRespDto resp;

    public AttamptRespDto getResp() {
        return resp;
    }

    public void setResp(AttamptRespDto resp) {
        this.resp = resp;
    }
}
