package cn.chono.yopper.Service.Http.GetPrize;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/3/21.
 */
public class GetPrizeService extends HttpService {
    GetPrizeReqBean getPrizeReqBean;
    public GetPrizeService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = GetPrizeRespBean.class;

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userPrizeId",getPrizeReqBean.getUserPrizeId());
        hashMap.put("userAddress",getPrizeReqBean.getUserAddress());

        callWrap = OKHttpUtils.post(context, HttpURL.get_prize,hashMap,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        getPrizeReqBean = (GetPrizeReqBean)iBean;
    }
}
