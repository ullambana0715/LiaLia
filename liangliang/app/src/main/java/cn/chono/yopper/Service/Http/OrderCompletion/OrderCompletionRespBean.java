package cn.chono.yopper.Service.Http.OrderCompletion;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class OrderCompletionRespBean extends RespBean {

    private OrderCompletionDto resp;

    public OrderCompletionDto getResp() {
        return resp;
    }

    public void setResp(OrderCompletionDto resp) {
        this.resp = resp;
    }


}
