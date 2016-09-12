package cn.chono.yopper.Service.Http.OrderDetail;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class OrderDetailRespBean extends RespBean {

    private OrderDetailDto resp;

    public OrderDetailDto getResp() {
        return resp;
    }

    public void setResp(OrderDetailDto resp) {
        this.resp = resp;
    }


}
