package cn.chono.yopper.Service.Http.MyEnergy;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/3/18.
 */
public class EnergyService extends HttpService {
    public EnergyService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = EnergyRespBean.class;
        callWrap = OKHttpUtils.get(context, HttpURL.energy, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
