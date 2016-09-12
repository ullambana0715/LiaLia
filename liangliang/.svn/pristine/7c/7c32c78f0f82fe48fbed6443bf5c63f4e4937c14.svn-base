package cn.chono.yopper.Service.Http.UserLogin;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 登陆
 * Created by zxb on 2015/11/20.
 */
public class UserLoginService extends HttpService {

    public UserLoginService(Context context) {
        super(context);
    }

    private UserLoginBean iBean;

    @Override
    public void enqueue() {
        OutDataClass = UserLoginRespBean.class;
        HashMap<String,Object> HashMap=new HashMap<>();
        HashMap.put("mobile",iBean.getMobile());
        HashMap.put("hashedPassword",iBean.getHashedPassword());
        callWrap= OKHttpUtils.post(context, HttpURL.login,HashMap,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean = (UserLoginBean) iBean;
    }
}
