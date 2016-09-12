package cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by sunquan on 16/4/28.
 */
public class AlipayGetSignRespBean extends RespBean {

    private AlipayGetSignResultsDto resp;

    public AlipayGetSignResultsDto getResp() {
        return resp;
    }

    public void setResp(AlipayGetSignResultsDto resp) {
        this.resp = resp;
    }


}
