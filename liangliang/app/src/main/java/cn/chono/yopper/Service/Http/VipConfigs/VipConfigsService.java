package cn.chono.yopper.Service.Http.VipConfigs;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/6/14.
 */
public class VipConfigsService extends HttpService {

    public VipConfigsService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {

        OutDataClass = VipConfigsRespEntity.class;

        OKHttpUtils.get(context, HttpURL.vipConfigs, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
