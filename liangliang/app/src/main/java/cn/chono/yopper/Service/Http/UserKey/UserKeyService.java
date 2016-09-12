package cn.chono.yopper.Service.Http.UserKey;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 用户购买钥匙
 * Created by cc on 16/5/13.
 */
public class UserKeyService extends HttpService {

    public UserKeyService(Context context) {
        super(context);
    }

    UserKeyBean mUserKeyBean;

    @Override
    public void enqueue() {

        OutDataClass=UserKeyRespEntity.class;

        HashMap<String,Object> hashMap=new HashMap<>();


        hashMap.put("keyCount",mUserKeyBean.keyCount);

        OKHttpUtils.post(context, HttpURL.userkey,hashMap,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

        mUserKeyBean= (UserKeyBean) iBean;

    }
}
