package cn.chono.yopper.Service.Http.DatingsTravelConfigs;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/4/5.
 */
public class DatingsTravelConfigsService extends HttpService {
    public DatingsTravelConfigsService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {


       OutDataClass=DatingsTravelConfigsResp.class;

        callWrap= OKHttpUtils.get(context, HttpURL.dating_travelConfigs,okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
