package cn.chono.yopper.Service.Http.SubmitVideo;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class SubmitVideoService extends HttpService {
    public SubmitVideoService(Context context) {
        super(context);
    }

    private SubmitVideoBean videoBean;

    @Override
    public void enqueue() {
        OutDataClass = SubmitVideoRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();


        HashMap.put("purpose", videoBean.getPurpose());
        HashMap.put("videoUrl", videoBean.getVideoUrl());
        HashMap.put("coverImgUrl", videoBean.getCoverImgUrl());

        String url = HttpURL.verification_user + videoBean.getUserId();
        callWrap = OKHttpUtils.post(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        videoBean = (SubmitVideoBean) iBean;
    }
}
