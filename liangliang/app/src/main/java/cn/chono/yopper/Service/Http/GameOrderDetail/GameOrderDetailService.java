package cn.chono.yopper.Service.Http.GameOrderDetail;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 *
 *订单详情
 */
public class GameOrderDetailService extends HttpService {
    public GameOrderDetailService(Context context) {
        super(context);
    }


    private GameOrderDetailBean nearbyBean;
    @Override
    public void enqueue() {

        OutDataClass=GameOrderDetailRespBean.class;

        String url = HttpURL.game_order_detail +nearbyBean.getId()+"/bling";

        callWrap= OKHttpUtils.get(context, url,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearbyBean= (GameOrderDetailBean) iBean;
    }
}
