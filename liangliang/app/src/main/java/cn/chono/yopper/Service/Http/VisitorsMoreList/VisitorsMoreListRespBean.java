package cn.chono.yopper.Service.Http.VisitorsMoreList;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.VisitorsListDto;

/**
 * Created by zxb on 2015/11/23.
 */
public class VisitorsMoreListRespBean extends RespBean {
    private VisitorsListDto resp;

    public VisitorsListDto getResp() {
        return resp;
    }

    public void setResp(VisitorsListDto resp) {
        this.resp = resp;
    }
}
