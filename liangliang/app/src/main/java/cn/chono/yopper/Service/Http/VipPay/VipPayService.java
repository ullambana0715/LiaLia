package cn.chono.yopper.Service.Http.VipPay;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/6/14.
 */
public class VipPayService extends HttpService {

    public VipPayService(Context context) {
        super(context);
    }


    VipPayBean mVipPayBean;


    @Override
    public void enqueue() {

        OutDataClass = VipPayRespEntity.class;

        StringBuffer sb = new StringBuffer(HttpURL.vipPay);

        sb.append(mVipPayBean.userId);
        sb.append("/UserPosition");

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("userPosition", mVipPayBean.userPosition);

        OKHttpUtils.post(context, sb.toString(), hashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {

        mVipPayBean = (VipPayBean) iBean;

    }
}
