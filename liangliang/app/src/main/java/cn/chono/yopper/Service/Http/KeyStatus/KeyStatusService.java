package cn.chono.yopper.Service.Http.KeyStatus;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/21.
 */
public class KeyStatusService extends HttpService {
    public KeyStatusService(Context context) {
        super(context);
    }

    private KeyStatusBean bean;

    @Override
    public void enqueue() {
        OutDataClass = KeyStatusRespBean.class;


        String url = HttpURL.get_key_status + "TargetUserId=" + bean.getTargetUserId();

        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

        bean = (KeyStatusBean) iBean;
    }
}
