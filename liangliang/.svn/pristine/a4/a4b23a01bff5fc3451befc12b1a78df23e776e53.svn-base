package cn.chono.yopper.Service.Http.UserInfoID;

import android.content.Context;

import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;

/**
 * 根据id获取用户基本信息
 * Created by zxb on 2015/11/20.
 */
public class UserInfoIDService extends HttpService {
    public UserInfoIDService(Context context) {
        super(context);
    }

    private UserInfoIDBean infoIDBean;

    @Override
    public void enqueue() {
        OutDataClass = UserInfoIDRespBean.class;

        String url = HttpURL.get_user_info_with_id + infoIDBean.getUserId();
        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        infoIDBean = (UserInfoIDBean) iBean;

    }
}
