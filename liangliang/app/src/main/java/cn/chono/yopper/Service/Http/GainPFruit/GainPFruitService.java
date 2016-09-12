package cn.chono.yopper.Service.Http.GainPFruit;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 苹果（积分）及钥匙信息获取
 * Created by zxb on 2015/11/23.
 */
public class GainPFruitService extends HttpService {
    public GainPFruitService(Context context) {
        super(context);
    }

    private GainPFruitBean fruitBean;

    @Override
    public void enqueue() {
        OutDataClass = GainPFruitRespEntity.class;

        String url = HttpURL.get_p_fruit_point + fruitBean.getUserId() + "/points";

        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        fruitBean = (GainPFruitBean) iBean;
    }
}
