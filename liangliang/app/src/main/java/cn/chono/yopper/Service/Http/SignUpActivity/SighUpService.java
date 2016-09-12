package cn.chono.yopper.Service.Http.SignUpActivity;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/6/15.
 */
public class SighUpService extends HttpService {
    SignUpReqBean reqBean;
    public SighUpService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = SighUpRespBean.class;
        String url = HttpURL.signup + reqBean.getId() + "/join?useFree="+reqBean.isUseFee();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",reqBean.getId());
        hashMap.put("useFree",reqBean.isUseFee());
        callWrap = OKHttpUtils.put(context,url,hashMap,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        reqBean = (SignUpReqBean)iBean;
    }
}
