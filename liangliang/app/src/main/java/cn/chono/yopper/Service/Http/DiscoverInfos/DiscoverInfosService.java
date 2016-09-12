package cn.chono.yopper.Service.Http.DiscoverInfos;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/6/14.
 */
public class DiscoverInfosService extends HttpService {
    public DiscoverInfosService(Context context) {
        super(context);
    }

    DiscoverInfosBean mDiscoverInfosBean;

    @Override
    public void enqueue() {

        OutDataClass = DiscoverInfosRespEntity.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("city", mDiscoverInfosBean.city);


        OKHttpUtils.get(context, HttpURL.DiscoverInfos, hashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {

        mDiscoverInfosBean = (DiscoverInfosBean) iBean;
        ;
    }
}
