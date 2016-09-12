package cn.chono.yopper.Service.Http.VideoMusicList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class VideoMusicListService extends HttpService {
    public VideoMusicListService(Context context) {
        super(context);
    }

    private VideoMusicListBean iBean;

    @Override
    public void enqueue() {
        OutDataClass = VideoMusicListRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("start", iBean.getStart());

        callWrap = OKHttpUtils.get(context, HttpURL.video_music, HashMap, okHttpListener);

      
    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean = (VideoMusicListBean) iBean;


    }
}
