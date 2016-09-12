package cn.chono.yopper.Service.Http.ExpiryDate;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/3/15.
 */
public class ExpiryDateService extends HttpService {

    public ExpiryDateService(Context context) {
        super(context);
    }

    private ExpiryDateBean dateBean;

    @Override
    public void enqueue() {

        OutDataClass = ExpiryDateRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("prizeId", dateBean.getPrizeId());

        hashMap.put("userAddress" , dateBean.getUserAddress());

        callWrap = OKHttpUtils.post(context, HttpURL.expiry_result, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        dateBean = (ExpiryDateBean) iBean;
    }
}
