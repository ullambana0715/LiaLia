package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by cc on 16/3/22.
 */
public class TravelPay implements Serializable {

    private String theWay;
    private String pay;

    public String getTheWay() {
        return theWay;
    }

    public void setTheWay(String theWay) {
        this.theWay = theWay;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
