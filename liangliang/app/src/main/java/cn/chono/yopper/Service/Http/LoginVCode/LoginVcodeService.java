package cn.chono.yopper.Service.Http.LoginVCode;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 短信验证码--登录
 * Created by zxb on 2015/11/19.
 */
public class LoginVcodeService extends HttpService {

    private LoginVcodeBean iBean;

    public LoginVcodeService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass=LoginVcodeRespBean.class;

        HashMap<String,Object> HashMap=new HashMap<>();
        HashMap.put("mobile",iBean.getMobile()); HashMap.put("verifyCode", iBean.getVerifyCode());
        callWrap= OKHttpUtils.post(context, HttpURL.login_vcode,HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
       this.iBean= (LoginVcodeBean) iBean;
    }
}
