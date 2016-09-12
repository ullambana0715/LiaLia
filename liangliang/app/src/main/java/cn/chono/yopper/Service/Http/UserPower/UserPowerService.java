package cn.chono.yopper.Service.Http.UserPower;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/3/15.
 */
public class UserPowerService extends HttpService {

    public UserPowerService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {

        OutDataClass = UserPowerRespBean.class;

        callWrap = OKHttpUtils.get(context, HttpURL.now_user_power, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
