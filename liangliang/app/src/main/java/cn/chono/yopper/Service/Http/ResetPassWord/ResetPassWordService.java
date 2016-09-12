package cn.chono.yopper.Service.Http.ResetPassWord;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 重设密码
 * Created by zxb on 2015/11/20.
 */
public class ResetPassWordService extends HttpService {
    public ResetPassWordService(Context context) {
        super(context);
    }

    private ResetPassWordBean iBean;

    @Override
    public void enqueue() {
        OutDataClass=ResetPassWordRespBean.class;
        HashMap<String,Object> HashMap=new HashMap<>();
        HashMap.put("hashedNewPassword",iBean.getConfirmHashedPassword());
        HashMap.put("confirmHashedPassword",iBean.getHashedNewPassword());
        callWrap= OKHttpUtils.put(context, HttpURL.user_password, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean = (ResetPassWordBean) iBean;
    }
}
