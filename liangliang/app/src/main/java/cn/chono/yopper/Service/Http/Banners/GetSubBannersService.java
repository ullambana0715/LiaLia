package cn.chono.yopper.Service.Http.Banners;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/2/22.
 */
public class GetSubBannersService extends HttpService {
    public GetSubBannersService(Context context) {
        super(context);
    }

    private BannersBean bannersBean;
    @Override
    public void enqueue() {


        OutDataClass = BannersRespBean.class;
        String url=HttpURL.subbanners+bannersBean.getParentId();

        callWrap = OKHttpUtils.get(context,url,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        bannersBean = (BannersBean) iBean;
    }
}
