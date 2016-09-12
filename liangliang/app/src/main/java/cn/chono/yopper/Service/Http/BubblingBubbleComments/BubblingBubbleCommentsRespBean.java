package cn.chono.yopper.Service.Http.BubblingBubbleComments;

import cn.chono.yopper.data.EvaluateBubbling;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * 提交评论
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubbleCommentsRespBean extends RespBean {
    private EvaluateBubbling resp;

    public EvaluateBubbling getResp() {
        return resp;
    }

    public void setResp(EvaluateBubbling resp) {
        this.resp = resp;
    }
}
