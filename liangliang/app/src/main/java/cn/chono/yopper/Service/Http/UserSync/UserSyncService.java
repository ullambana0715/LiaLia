package cn.chono.yopper.Service.Http.UserSync;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 获取当前用户的碰友
 * Created by zxb on 2015/11/20.
 */
public class UserSyncService extends HttpService {
    public UserSyncService(Context context) {
        super(context);
    }

    private UserSyncBean iBean;

    @Override
    public void enqueue() {
        OutDataClass = UserSyncRespBean.class;
        String url = HttpURL.sync + iBean.getUserid() + "/sync";
        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean = (UserSyncBean) iBean;
    }
}
