package cn.chono.yopper.Service.Http.ExchangeBonus;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/3/21.
 */
public class ExchangeBonusService extends HttpService {
    ExchangeBonusReqBean exchangeBonusReqBean;
    public ExchangeBonusService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ExchangeBonusRespBean.class;
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("start",exchangeBonusReqBean.getStart());
        callWrap = OKHttpUtils.get(context, HttpURL.exchange_bonus_list, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        exchangeBonusReqBean = (ExchangeBonusReqBean)iBean;
    }
}
