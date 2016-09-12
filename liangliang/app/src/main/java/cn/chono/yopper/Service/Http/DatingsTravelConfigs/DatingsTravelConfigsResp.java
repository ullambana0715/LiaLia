package cn.chono.yopper.Service.Http.DatingsTravelConfigs;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/4/5.
 */
public class DatingsTravelConfigsResp extends RespBean {

   private TravelConfigsData resp;

    public TravelConfigsData getResp() {
        return resp;
    }

    public void setResp(TravelConfigsData resp) {
        this.resp = resp;
    }
}
