package cn.chono.yopper.Service.Http.Login3rd;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.andbase.tractor.http.HttpBase;
import com.andbase.tractor.http.OKHttp;
import com.andbase.tractor.listener.LoadListener;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.InviteType.InviteTypeBean;
import cn.chono.yopper.Service.Http.InviteType.InviteTypeRespBean;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.activity.register.RegisterWriteInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.wxapi.WeixinUtils;

/**
 * Created by cc on 16/1/27.
 */
public class Login3rdLogicService extends HttpService {


    private Login3rdBean login3rdBean;

    public Login3rdLogicService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {

        OutDataClass = Login3rdRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();

        String url = HttpURL.user_login3rd;

        HashMap.put("vendor", login3rdBean.getVendor());

        HashMap.put("openId", login3rdBean.getOpenId());

        if (login3rdBean.getVendor() == 1) {
            HashMap.put("code", login3rdBean.getCode());
        }

        HashMap.put("token", login3rdBean.getToken());

        callWrap = OKHttpUtils.post(context, url, HashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        login3rdBean = (Login3rdBean) iBean;

    }
}
