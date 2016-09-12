package cn.chono.yopper.Service.Http.MyActivitiesMoreList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by jianghua on 2016/6/13.
 */
public class MyActivityMoreService extends HttpService {

    private MyActivityMoreReq myActivityMoreReq;

    public MyActivityMoreService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = MyActivityMoreResp.class;

        HashMap<String, Object> hashMap = new HashMap<>();
        String url = myActivityMoreReq.getNextQuery();
        callWrap = OKHttpUtils.get(context, url, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        myActivityMoreReq = (MyActivityMoreReq) iBean;
    }
}
