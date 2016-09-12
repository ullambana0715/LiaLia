package cn.chono.yopper.Service.Http.VerifiCation;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.utils.HttpURL;

/**
 * 验证手机验证码
 * Created by zxb on 2015/11/19.
 */
public class VerifiCationService extends HttpService{

    private VerifiCationBean iBean;

    public VerifiCationService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {

        OutDataClass=VerifiCationRespBean.class;

        HashMap<String,Object> HashMap=new HashMap<>();
        HashMap.put("mobile",iBean.getMobile()); HashMap.put("verifyCode", iBean.getVerifyCode());
        callWrap= OKHttpUtils.post(context, HttpURL.request_verify, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
       this.iBean= (VerifiCationBean) iBean;
    }


    @Override
    protected void onCallFail(RespBean respBean) {
        super.onCallFail(respBean);
    }

    @Override
    protected void onCallSucceed(RespBean respBean) {
        super.onCallSucceed( respBean);
    }
}
