package cn.chono.yopper.Service.Http.OrderList;

import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailDto;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.OrderListDto;

/**
 * Created by zxb on 2015/11/22.
 */
public class OrderListRespBean extends RespBean {

    private OrderListDto resp;

    public OrderListDto getResp() {
        return resp;
    }

    public void setResp(OrderListDto resp) {
        this.resp = resp;
    }


}
