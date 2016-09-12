package cn.chono.yopper.Service.Http.GameOrderDetail;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class GameOrderDetailRespBean extends RespBean {

    private GameOrderDetailDto resp;

    public GameOrderDetailDto getResp() {
        return resp;
    }

    public void setResp(GameOrderDetailDto resp) {
        this.resp = resp;
    }


}
