package cn.chono.yopper.Service.Http.GainVerifiCode;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/19.
 */
public class GainVerifiCodeService extends HttpService{

    private  GainVerifiCodeBean iBean;

    public GainVerifiCodeService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass=GainVerifiCodeRespBean.class;
        HashMap<String,Object> HashMap=new HashMap<>();
        HashMap.put("mobile",iBean.getMobile());
        OKHttpUtils.post(context, HttpURL.smscode,HashMap,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean= (GainVerifiCodeBean) iBean;
    }
}
