package cn.chono.yopper.Service.Http.BubblingBubbleLikes;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 点赞列表
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubbleLikesService extends HttpService {
    public BubblingBubbleLikesService(Context context) {
        super(context);
    }

    private BubblingBubbleLikesBean likesBean;

    @Override
    public void enqueue() {
        OutDataClass = BubblingBubbleLikesRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("start", likesBean.getStart());
        HashMap.put("rows", likesBean.getRows());


        String url = HttpURL.bubbling_bubble + "/" + likesBean.getId() + "/likes?";

        callWrap = OKHttpUtils.get(context, url, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {

        likesBean = (BubblingBubbleLikesBean) iBean;
    }
}
