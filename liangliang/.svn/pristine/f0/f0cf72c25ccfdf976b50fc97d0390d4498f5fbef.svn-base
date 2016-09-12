package cn.chono.yopper.Service.Http.SubitUserInfo;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 注册--用户
 * Created by zxb on 2015/11/20.
 */
public class SubitUserInfoService extends HttpService {
    public SubitUserInfoService(Context context) {
        super(context);
    }

    private SubitUserInfoBean iBean;

    @Override
    public void enqueue() {
        OutDataClass=SubitUserInfoRespBean.class;

        HashMap<String,Object> HashMap=new HashMap<>();


        HashMap.put("name", iBean.getName());
        HashMap.put("mobile", iBean.getMobile());
        HashMap.put("horoscope", iBean.getHoroscope());
        HashMap.put("sex", iBean.getSex());
        HashMap.put("verifyCode", iBean.getVerifyCode());
        HashMap.put("headImg", iBean.getHeadImg());
        HashMap.put("albumImg", iBean.getAlbumImg());
        HashMap.put("lat", iBean.get_latitude());
        HashMap.put("lng", iBean.get_longtitude());

        if (!TextUtils.isEmpty(iBean.getHashedPassword())){
            HashMap.put("hashedPassword", iBean.getHashedPassword());
        }

        callWrap= OKHttpUtils.post(context,HttpURL.register_user,HashMap,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean= (SubitUserInfoBean) iBean;
    }
}
