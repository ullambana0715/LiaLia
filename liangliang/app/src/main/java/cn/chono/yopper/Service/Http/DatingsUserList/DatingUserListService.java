package cn.chono.yopper.Service.Http.DatingsUserList;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingUserListService extends HttpService {
    public DatingUserListService(Context context) {
        super(context);
    }

    private DatingUserListBean nearestBean;

    @Override
    public void enqueue() {
        OutDataClass = DatingUserListRespBean.class;

        callWrap = OKHttpUtils.get(context, HttpURL.get_v3_dating_user_list+nearestBean.getDatingId()+"/users", okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearestBean = (DatingUserListBean) iBean;
    }

}
