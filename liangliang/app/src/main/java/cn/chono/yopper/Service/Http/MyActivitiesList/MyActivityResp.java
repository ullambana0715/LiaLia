package cn.chono.yopper.Service.Http.MyActivitiesList;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by jianghua on 2016/6/12.
 */
public class MyActivityResp extends RespBean {

    private ActivityRespBean resp;

    public ActivityRespBean getResp() {
        return resp;
    }

    public void setResp(ActivityRespBean resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "MyActivityResp{" +
                "resp=" + resp +
                '}';
    }
}
