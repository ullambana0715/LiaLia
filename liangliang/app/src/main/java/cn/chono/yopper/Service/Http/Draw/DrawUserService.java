package cn.chono.yopper.Service.Http.Draw;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/4/7.
 */
public class DrawUserService extends HttpService {
    public DrawUserService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = DrawUserRespBean.class;
        callWrap = OKHttpUtils.post(context, HttpURL.draw_user_list, null,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
