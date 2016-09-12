package cn.chono.yopper.Service.Http.MyActivitiesList;

import android.content.Context;

import com.lidroid.xutils.util.LogUtils;
import com.squareup.okhttp.HttpUrl;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/6/12.
 */
public class MyActivityService extends HttpService {

    private MyActivityReq myActivityReq;

    public MyActivityService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = MyActivityResp.class;

        String url = HttpURL.mine_activities+"Start="+myActivityReq.getStart();

        callWrap = OKHttpUtils.get(context, url,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        myActivityReq = (MyActivityReq) iBean;
    }
}
