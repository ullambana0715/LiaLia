package cn.chono.yopper.Service.Http.DatingsClose;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;


public class DatingsCloseService extends HttpService {

    public DatingsCloseService(Context context) {
        super(context);
    }

    private DatingsCloseBean joinBean;

    @Override
    public void enqueue() {
        OutDataClass = DatingsCloseRespBean.class;

        String url = HttpURL.close_dating + joinBean.getId();

        callWrap = OKHttpUtils.delete(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        joinBean = (DatingsCloseBean) iBean;
    }
}
