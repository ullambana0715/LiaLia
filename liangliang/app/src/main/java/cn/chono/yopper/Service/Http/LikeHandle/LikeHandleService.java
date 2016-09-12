package cn.chono.yopper.Service.Http.LikeHandle;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 *
 * Created by zxb on 2015/11/22.
 */
public class LikeHandleService extends HttpService {
    public LikeHandleService(Context context) {
        super(context);
    }

    private LikeHandleBean requestBean;

    @Override
    public void enqueue() {
        OutDataClass = LikeHandleRespBean.class;

        HashMap<String, Object> linkedHashMap = new HashMap<>();
        linkedHashMap.put("islike", requestBean.islike());

        String url = HttpURL.like_handle + requestBean.getUserId() + "/like";

        callWrap = OKHttpUtils.post(context, url, linkedHashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        requestBean = (LikeHandleBean) iBean;
    }
}
