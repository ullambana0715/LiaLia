package cn.chono.yopper.Service.Http.UsersNearbyMore;

import cn.chono.yopper.data.DiscoverPeopleDto;
import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class UsersNearbyMoreRespBean extends RespBean {
    private DiscoverPeopleDto resp;

    public DiscoverPeopleDto getResp() {
        return resp;
    }

    public void setResp(DiscoverPeopleDto resp) {
        this.resp = resp;
    }
}
