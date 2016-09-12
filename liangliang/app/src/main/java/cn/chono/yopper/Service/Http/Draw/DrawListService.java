package cn.chono.yopper.Service.Http.Draw;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/3/24.
 */
public class DrawListService extends HttpService {
    DrawReqBean drawReqBean;
    public DrawListService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = DrawListRespBean.class;

        callWrap = OKHttpUtils.get(context, HttpURL.draw_list, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        drawReqBean = (DrawReqBean)iBean;
    }
}
