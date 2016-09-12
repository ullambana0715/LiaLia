package cn.chono.yopper.Service.Http.ActivitySignStatus;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/7/1.
 */
public class ActivitySignService extends HttpService {
    ActivitySignReqBean reqBean;
    public ActivitySignService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ActivitySignRespBean.class;
        String url= HttpURL.activity_sign+reqBean.getActivityId()+"/status";

        callWrap= OKHttpUtils.get(context, url,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        reqBean = (ActivitySignReqBean)iBean;
    }
}
