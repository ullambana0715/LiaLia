package cn.chono.yopper.Service.Http.ChatDatingUserAttampt;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.entity.AttamptRespDto;

/**
 * Created by zxb on 2015/11/23.
 */
public class ChatDatingUserAttamptRespBean extends RespBean {
    private AttamptRespDto resp;

    public AttamptRespDto getResp() {
        return resp;
    }

    public void setResp(AttamptRespDto resp) {
        this.resp = resp;
    }
}