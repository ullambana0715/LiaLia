package cn.chono.yopper.Service.Http.OrderProduct;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 商品下单（购买苹果、星钻）
 * Created by cc on 16/5/6.
 */
public class OrderProductService extends HttpService {
    public OrderProductService(Context context) {
        super(context);
    }

    OrderProductBean mOrderProductBean;

    @Override
    public void enqueue() {

        OutDataClass = OrderProductRespEntity.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("orderType", mOrderProductBean.orderType);
        hashMap.put("productId", mOrderProductBean.productId);

        OKHttpUtils.post(context, HttpURL.orders_product, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

        mOrderProductBean = (OrderProductBean) iBean;

    }
}
