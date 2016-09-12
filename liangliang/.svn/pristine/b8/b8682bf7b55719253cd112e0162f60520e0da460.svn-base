package cn.chono.yopper.Service.Http.GainVersionInfo;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 获取版本信息
 * Created by zxb on 2015/11/20.
 */
public class GainVersionInfoService extends HttpService {
    public GainVersionInfoService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {

        OutDataClass = GainVersionInfoRespBean.class;

        callWrap = OKHttpUtils.get(context, HttpURL.get_version_info, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
