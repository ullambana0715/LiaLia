package cn.chono.yopper.Service.Http.VideoMusicList;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.VideoMusicList;

/**
 * Created by zxb on 2015/11/23.
 */
public class VideoMusicListRespBean extends RespBean {
    private VideoMusicList resp;

    public VideoMusicList getResp() {
        return resp;
    }

    public void setResp(VideoMusicList resp) {
        this.resp = resp;
    }
}
