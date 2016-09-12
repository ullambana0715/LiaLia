package cn.chono.yopper.Service.Http.VideoMusicMoreList;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/23.
 */
public class VideoMusicMoreListService extends HttpService {
    public VideoMusicMoreListService(Context context) {
        super(context);
    }

    private VideoMusicMoreListBean iBean;

    @Override
    public void enqueue() {
        OutDataClass = VideoMusicMoreListRespBean.class;


        callWrap = OKHttpUtils.get(context, iBean.getNextQuery(), okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean = (VideoMusicMoreListBean) iBean;


    }
}
