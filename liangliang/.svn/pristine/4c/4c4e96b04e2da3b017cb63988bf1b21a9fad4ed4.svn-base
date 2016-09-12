package cn.chono.yopper.Service.Http.PublishWish;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 发布约会
 * Created by zxb on 2015/11/21.
 */
public class PublishWishService extends HttpService {
    public PublishWishService(Context context) {
        super(context);
    }

    PublishWishBean wishBean;

    @Override
    public void enqueue() {
        OutDataClass = PublishWishRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();

        HashMap.put("content", wishBean.getContent());
        HashMap.put("lat", wishBean.getLat());
        HashMap.put("lng", wishBean.getLng());
        HashMap.put("datingType", wishBean.getDatingType());
        HashMap.put("locationId", wishBean.getLocationId());

        callWrap = OKHttpUtils.post(context, HttpURL.publish_wish, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        wishBean = (PublishWishBean) iBean;
    }
}
