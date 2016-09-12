package cn.chono.yopper.Service.Http.BubblingBubblePraise;

import java.io.Serializable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 *  点赞请求
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubblePraiseRespBean extends RespBean {
    private BubblingBubblePraiseRespDto resp;

    public BubblingBubblePraiseRespDto getResp() {
        return resp;
    }

    public void setResp(BubblingBubblePraiseRespDto resp) {
        this.resp = resp;
    }

    public  class BubblingBubblePraiseRespDto implements Serializable{
        private String bubbleId;

        public String getBubbleId() {
            return bubbleId;
        }

        public void setBubbleId(String bubbleId) {
            this.bubbleId = bubbleId;
        }
    }
}
