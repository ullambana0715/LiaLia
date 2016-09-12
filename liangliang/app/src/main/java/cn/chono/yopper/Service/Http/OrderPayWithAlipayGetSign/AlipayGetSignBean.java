package cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by sunquan on 16/4/28.
 */
public class AlipayGetSignBean extends ParameterBean {


    private String out_trade_no;


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


}
