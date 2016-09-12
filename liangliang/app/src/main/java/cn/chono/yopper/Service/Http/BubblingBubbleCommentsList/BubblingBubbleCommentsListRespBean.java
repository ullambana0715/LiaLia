package cn.chono.yopper.Service.Http.BubblingBubbleCommentsList;

import cn.chono.yopper.data.BubblingEvaluateDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubbleCommentsListRespBean extends RespBean {
    private BubblingEvaluateDto resp;

    public BubblingEvaluateDto getResp() {
        return resp;
    }

    public void setResp(BubblingEvaluateDto resp) {
        this.resp = resp;
    }
}
