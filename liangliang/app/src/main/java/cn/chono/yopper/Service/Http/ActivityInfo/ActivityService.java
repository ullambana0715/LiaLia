package cn.chono.yopper.Service.Http.ActivityInfo;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/6/15.
 */
public class ActivityService extends HttpService {
    ActivityReqBean reqBean;
    public ActivityService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ActivityRespBean.class;
        String url= HttpURL.activity_info+reqBean.getId()+"/join";
        callWrap = OKHttpUtils.get(context, url,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        reqBean = (ActivityReqBean)iBean;
    }
}
