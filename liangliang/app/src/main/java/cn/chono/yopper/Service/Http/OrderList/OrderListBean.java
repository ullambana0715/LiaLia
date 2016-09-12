package cn.chono.yopper.Service.Http.OrderList;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class OrderListBean extends ParameterBean {

    private int orderType;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
