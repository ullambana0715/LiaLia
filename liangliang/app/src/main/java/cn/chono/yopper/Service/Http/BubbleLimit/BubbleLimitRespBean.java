package cn.chono.yopper.Service.Http.BubbleLimit;

import cn.chono.yopper.data.ChatAttamptRespDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class BubbleLimitRespBean extends RespBean {
    private ChatAttamptRespDto resp;

    public ChatAttamptRespDto getResp() {
        return resp;
    }

    public void setResp(ChatAttamptRespDto resp) {
        this.resp = resp;
    }
}
