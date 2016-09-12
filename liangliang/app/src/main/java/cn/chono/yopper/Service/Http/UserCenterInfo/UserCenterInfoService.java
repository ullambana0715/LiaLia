package cn.chono.yopper.Service.Http.UserCenterInfo;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/3/17.
 */
public class UserCenterInfoService extends HttpService {
    public UserCenterInfoService(Context context) {
        super(context);
    }

    private UserCenterInfoBean centerInfoBean;

    @Override
    public void enqueue() {
        OutDataClass = UserCenterInfoRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        callWrap = OKHttpUtils.get(context, HttpURL.user_center_info, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        centerInfoBean = (UserCenterInfoBean) iBean;

    }
}
