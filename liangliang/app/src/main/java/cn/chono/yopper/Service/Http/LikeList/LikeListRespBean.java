package cn.chono.yopper.Service.Http.LikeList;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.BlockListDto;
import cn.chono.yopper.data.LikeListDto;

/**
 * Created by zxb on 2015/11/22.
 */
public class LikeListRespBean extends RespBean {

    private LikeListDto resp;

    public LikeListDto getResp() {
        return resp;
    }

    public void setResp(LikeListDto resp) {
        this.resp = resp;
    }
}
