package cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by sunquan on 16/4/28.
 */
public class WXGetSignBean extends ParameterBean {


    private String orderid;


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

}
