package cn.chono.yopper.Service.Http.DaillyTouch;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 每日首次报到
 * Created by zxb on 2015/11/20.
 */
public class DaillyTouchService extends HttpService {
    public DaillyTouchService(Context context) {
        super(context);
    }

    private DaillyTouchBean iBean;

    @Override
    public void enqueue() {
        OutDataClass = DaillyTouchRespBean.class;

        String url = HttpURL.dailly_touch + iBean.getUserId() + "/dailly";
        callWrap = OKHttpUtils.post(context, url, new HashMap<String, Object>(), okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean = (DaillyTouchBean) iBean;
    }
}
