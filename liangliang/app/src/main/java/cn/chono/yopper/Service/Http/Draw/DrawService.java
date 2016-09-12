package cn.chono.yopper.Service.Http.Draw;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/3/25.
 */
public class DrawService extends HttpService {
    private DrawReqBean drawReqBean;
    public DrawService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = DrawRespBean.class;
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userPrizeId",drawReqBean.getUserPrizeId());
        String url = HttpURL.draw + "userPrizeId="+drawReqBean.getUserPrizeId();
        callWrap = OKHttpUtils.post(context, url, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        drawReqBean = (DrawReqBean)iBean;
    }
}
