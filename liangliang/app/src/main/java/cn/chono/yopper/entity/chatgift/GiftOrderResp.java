package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class GiftOrderResp implements Serializable {

    private int result;
    private String msg;

    private OrderBean order;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "GiftOrderResp{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", order=" + order +
                '}';
    }
}
