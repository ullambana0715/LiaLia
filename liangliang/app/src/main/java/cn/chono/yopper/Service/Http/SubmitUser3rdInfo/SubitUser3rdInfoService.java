package cn.chono.yopper.Service.Http.SubmitUser3rdInfo;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/1/27.
 */
public class SubitUser3rdInfoService extends HttpService {
    public SubitUser3rdInfoService(Context context) {
        super(context);
    }

    private SubitUser3rdInfoBean user3rdInfoBean;

    @Override
    public void enqueue() {

        OutDataClass=SubitUser3rdInfoRespBean.class;

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("name",user3rdInfoBean.getName());
        hashMap.put("vendor",user3rdInfoBean.getVendor());
        hashMap.put("openId",user3rdInfoBean.getOpenId());
        hashMap.put("accessToken",user3rdInfoBean.getAccessToken());
        hashMap.put("horoscope",user3rdInfoBean.getHoroscope());
        hashMap.put("sex",user3rdInfoBean.getSex());
        hashMap.put("headImg",user3rdInfoBean.getHeadImg());
        hashMap.put("lat",user3rdInfoBean.getLat());
        hashMap.put("lng",user3rdInfoBean.getLng());
        hashMap.put("identity",user3rdInfoBean.getIdentity());

        callWrap= OKHttpUtils.post(context, HttpURL.user_register3rd,hashMap,okHttpListener);



    }

    @Override
    public void parameter(ParameterBean iBean) {
        user3rdInfoBean= (SubitUser3rdInfoBean) iBean;
    }
}
