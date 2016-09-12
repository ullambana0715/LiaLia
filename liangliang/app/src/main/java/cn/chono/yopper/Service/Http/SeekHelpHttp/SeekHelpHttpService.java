package cn.chono.yopper.Service.Http.SeekHelpHttp;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 注册时收不到验证码的话，用户点击fasthelp可以向运营人员求助，
 * Created by zxb on 2015/11/19.
 */
public class SeekHelpHttpService extends HttpService {
    private SeekHelpHttpBean iBean;


    public SeekHelpHttpService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass= SeekHelpHttpRespBean.class;
        HashMap<String,Object> HashMap=new HashMap<>();
        HashMap.put("mobile",iBean.getMobile());
        OKHttpUtils.post(context, HttpURL.fasthelp, HashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
            this.iBean= (SeekHelpHttpBean) iBean;
    }
}
