package cn.chono.yopper.Service.Http.BubblingDelete;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/22.
 */
public class BubblingDeleteService extends HttpService {
    public BubblingDeleteService(Context context) {
        super(context);
    }

    private BubblingDeleteBean deleteBean;

    @Override
    public void enqueue() {
        OutDataClass = BubblingDeleteRespBean.class;

        String url = HttpURL.bubbling_bubble + "/" + deleteBean.getBubblingId();
        callWrap = OKHttpUtils.delete(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        deleteBean = (BubblingDeleteBean) iBean;
    }
}
