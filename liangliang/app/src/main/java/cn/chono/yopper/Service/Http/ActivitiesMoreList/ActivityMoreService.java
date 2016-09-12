package cn.chono.yopper.Service.Http.ActivitiesMoreList;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by jianghua on 2016/6/14.
 */
public class ActivityMoreService extends HttpService {

    private ActivityMoreReq activityMoreReq;

    public ActivityMoreService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ActivityMoreResp.class;

        callWrap = OKHttpUtils.get(context, activityMoreReq.getNextQuery(), okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        activityMoreReq = (ActivityMoreReq) iBean;
    }
}
