package cn.chono.yopper.Service.Http.BubbleInfo;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/21.
 */
public class BubbleInfoService extends HttpService {
    public BubbleInfoService(Context context) {
        super(context);
    }

    private  BubbleInfoBean infoBean;

    @Override
    public void enqueue() {
        OutDataClass=BubbleInfoRespBean.class;

        HashMap<String,Object> linkedHashMap=new HashMap<>();

        linkedHashMap.put("Lat",infoBean.getLat());

        linkedHashMap.put("Lng",infoBean.getLng());

        String  url = HttpURL.bubbling_bubble + "/" + infoBean.getBubbleId()+"?";

        callWrap= OKHttpUtils.get(context,url,linkedHashMap,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

        infoBean= (BubbleInfoBean) iBean;
    }
}
