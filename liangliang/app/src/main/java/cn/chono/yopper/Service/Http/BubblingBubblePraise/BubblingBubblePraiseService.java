package cn.chono.yopper.Service.Http.BubblingBubblePraise;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 点赞请求
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubblePraiseService extends HttpService {
    public BubblingBubblePraiseService(Context context) {
        super(context);
    }

    private BubblingBubblePraiseBean praiseBean;

    @Override
    public void enqueue() {
        OutDataClass = BubblingBubblePraiseRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("like", praiseBean.isLike());

        String url = HttpURL.bubbling_bubble + "/" + praiseBean.getId() + "/like";

        callWrap = OKHttpUtils.post(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        praiseBean = (BubblingBubblePraiseBean) iBean;
    }
}
