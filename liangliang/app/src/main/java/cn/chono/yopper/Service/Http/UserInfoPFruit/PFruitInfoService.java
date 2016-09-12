package cn.chono.yopper.Service.Http.UserInfoPFruit;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/3/15.
 */
public class PFruitInfoService extends HttpService {

    public PFruitInfoService(Context context) {
        super(context);
    }

    private PFruitBean pFruitBean;

    @Override
    public void enqueue() {
        OutDataClass = PFruitRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        String url = HttpURL.pfruit_info + pFruitBean.getUserId() + "/points";

        callWrap = OKHttpUtils.get(context, url, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        pFruitBean = (PFruitBean) iBean;
    }
}
