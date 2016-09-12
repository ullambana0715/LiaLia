package cn.chono.yopper.Service.Http.DatingsMarriageLimit;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/4/6.
 */
public class DatingsMarriageLimitRespBean extends RespBean {

    private DatingsMarriageLimitData resp;


    public DatingsMarriageLimitData getResp() {
        return resp;
    }

    public void setResp(DatingsMarriageLimitData resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "DatingsMarriageLimitRespBean{" +
                "resp=" + resp +
                '}';
    }
}
