package cn.chono.yopper.Service.Http.OpenAlbumWithApple;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/7/26.
 */
public class OpenAlbumService extends HttpService {
    OpenAlbumReqBean bean;
    public OpenAlbumService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = OpenAlbumRespBean.class;
        HashMap hashMap = new HashMap();
        hashMap.put("userId",bean.getUserId());
        hashMap.put("lookedUserId",bean.getLookedUserId());
        callWrap = OKHttpUtils.put(context, HttpURL.private_album,hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        bean = (OpenAlbumReqBean)iBean;
    }
}
