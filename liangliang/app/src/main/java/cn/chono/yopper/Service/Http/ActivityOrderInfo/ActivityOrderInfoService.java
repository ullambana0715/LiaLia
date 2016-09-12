package cn.chono.yopper.Service.Http.ActivityOrderInfo;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/6/15.
 */
public class ActivityOrderInfoService extends HttpService {
    ActivityOrderInfoReqBean bean;
    public ActivityOrderInfoService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ActivityOrderInfoRespBean.class;
        String url = HttpURL.order_detail +bean.getId();

        callWrap= OKHttpUtils.get(context, url,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        bean = (ActivityOrderInfoReqBean)iBean;
    }
}
