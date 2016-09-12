package cn.chono.yopper.Service.Http.BlockList;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.BlockListDto;

/**
 * Created by zxb on 2015/11/22.
 */
public class BlockListRespBean extends RespBean {

    private BlockListDto resp;

    public BlockListDto getResp() {
        return resp;
    }

    public void setResp(BlockListDto resp) {
        this.resp = resp;
    }
}
