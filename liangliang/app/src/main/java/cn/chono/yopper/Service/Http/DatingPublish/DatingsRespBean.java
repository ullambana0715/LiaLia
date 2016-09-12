package cn.chono.yopper.Service.Http.DatingPublish;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/3/31.
 */
public class DatingsRespBean extends RespBean {

    private DatingsData resp;


    public DatingsData getResp() {
        return resp;
    }

    public void setResp(DatingsData resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "DatingsRespBean{" +
                "resp=" + resp +
                '}';
    }


}
