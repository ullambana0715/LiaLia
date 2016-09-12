package cn.chono.yopper.Service.Http.SubmitVideoOpen;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 是否公开视频
 * Created by zxb on 2015/11/23.
 */
public class SubmitVideoOpenService extends HttpService {
    public SubmitVideoOpenService(Context context) {
        super(context);
    }

    private SubmitVideoOpenBean openBean;

    @Override
    public void enqueue() {
        OutDataClass = SubmitVideoOpenRespBean.class;


        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("open", openBean.isOpen());

        String url = HttpURL.change_dating_purpose + openBean.getUserId();
        callWrap = OKHttpUtils.put(context, url, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        openBean = (SubmitVideoOpenBean) iBean;
    }
}
