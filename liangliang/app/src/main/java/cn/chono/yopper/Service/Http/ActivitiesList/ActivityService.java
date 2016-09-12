package cn.chono.yopper.Service.Http.ActivitiesList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/6/13.
 */
public class ActivityService extends HttpService {

    private ActivityReq activityReq;

    public ActivityService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ActivityResp.class;

        String url = HttpURL.index_activities + "Start=" + activityReq.getStart();
        callWrap = OKHttpUtils.get(context, url, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        activityReq = (ActivityReq) iBean;
    }
}
