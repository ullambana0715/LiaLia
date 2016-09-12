package cn.chono.yopper.Service.Http.Draw;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/6/13.
 */
public class DrawWithAppleService extends HttpService {
    public DrawWithAppleService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = DrawRespBean.class;
        callWrap = OKHttpUtils.post(context, HttpURL.draw_list,null, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
