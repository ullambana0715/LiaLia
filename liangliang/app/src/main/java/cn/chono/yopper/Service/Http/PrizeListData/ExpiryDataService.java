package cn.chono.yopper.Service.Http.PrizeListData;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/3/15.
 */
public class ExpiryDataService extends HttpService {

    public ExpiryDataService(Context context) {
        super(context);
    }

    private ExpiryDataBean dataBean;

    @Override
    public void enqueue() {
        OutDataClass = ExpiryDataRespBean.class;

        HashMap<String , Object> hashMap = new HashMap<>();

        hashMap.put("start",dataBean.getStart());

        callWrap = OKHttpUtils.get(context, HttpURL.expiry_date_list,hashMap,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        dataBean = (ExpiryDataBean)iBean;
    }
}
