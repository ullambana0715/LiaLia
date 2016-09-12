package cn.chono.yopper.Service.Http.UserMobileVerifyCode;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class UserMobileVerifyCodeService extends HttpService {

    public UserMobileVerifyCodeService(Context context) {
        super(context);
    }

    private UserMobileVerifyCodeBean bean;

    @Override
    public void enqueue() {
        OutDataClass = RespBean.class;

        String url = HttpURL.user_mobile_verification_code;
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("mobile", bean.getMobile());
        HashMap.put("verifyCode", bean.getVerifyCode());

        callWrap = OKHttpUtils.put(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        bean = (UserMobileVerifyCodeBean) iBean;
    }
}
