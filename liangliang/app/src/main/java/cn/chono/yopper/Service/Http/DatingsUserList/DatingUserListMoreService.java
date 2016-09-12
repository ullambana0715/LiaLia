package cn.chono.yopper.Service.Http.DatingsUserList;

import android.content.Context;

import cn.chono.yopper.Service.Http.DatingsList.DatingListRespBean;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingUserListMoreService extends HttpService {
    public DatingUserListMoreService(Context context) {
        super(context);
    }

    private DatingUserListMoreBean nearestsBean;

    @Override
    public void enqueue() {
        OutDataClass = DatingListRespBean.class;

        String url = nearestsBean.getNextQuery();
        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearestsBean = (DatingUserListMoreBean) iBean;
    }


}
