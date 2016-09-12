package cn.chono.yopper.Service.Http.BubblingBubble;

import android.content.Context;

import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.JsonUtils;

/**
 * 发布 冒泡
 * Created by zxb on 2015/11/22.
 */
public class BubblingBubbleService extends HttpService {
    public BubblingBubbleService(Context context) {
        super(context);
    }

    private BubblingBubbleBean bubbleBean;

    @Override
    public void enqueue() {
        OutDataClass = BubblingBubbleRespBean.class;

        HashMap<String, Object> linkedHashMap = new HashMap<>();
        linkedHashMap.put("imageUrls", bubbleBean.getImageUrls());
        linkedHashMap.put("addressName", bubbleBean.getAddressName());
        linkedHashMap.put("address", bubbleBean.getAddress());
        linkedHashMap.put("locId", bubbleBean.getLocId());
        linkedHashMap.put("lng", bubbleBean.getLng());
        linkedHashMap.put("lat", bubbleBean.getLat());
        linkedHashMap.put("text", bubbleBean.getText());


        String url = HttpURL.bubbling_bubble + "?" + "check" + "=" + bubbleBean.isCheck();




        callWrap = OKHttpUtils.post(context, url, linkedHashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        bubbleBean = (BubblingBubbleBean) iBean;
    }
}
