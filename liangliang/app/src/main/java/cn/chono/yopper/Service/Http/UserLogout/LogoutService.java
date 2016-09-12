package cn.chono.yopper.Service.Http.UserLogout;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 退出登录
 * Created by zxb on 2015/11/20.
 */
public class LogoutService extends HttpService {
    public LogoutService(Context context) {
        super(context);
    }

    private LogoutBean mLogoutBean;

    @Override
    public void enqueue() {
        OutDataClass=LogoutRespEntity.class;

        HashMap<String,Object> hashMap=new HashMap<>();



        StringBuilder sb=new StringBuilder(HttpURL.user_logout);

        sb.append("?");
        sb.append("force");
        sb.append("=");
        sb.append(mLogoutBean.force);

        callWrap= OKHttpUtils.post(context, sb.toString(), hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.mLogoutBean = (LogoutBean) iBean;
    }
}
