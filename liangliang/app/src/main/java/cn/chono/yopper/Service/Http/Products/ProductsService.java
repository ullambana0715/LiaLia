package cn.chono.yopper.Service.Http.Products;

import android.content.Context;

import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/5/5.
 */
public class ProductsService extends HttpService {
    public ProductsService(Context context) {
        super(context);
    }

    ProductsBean mProductsBean;

    @Override
    public void enqueue() {

        OutDataClass = ProductsRespEntity.class;

        StringBuilder sb = new StringBuilder(HttpURL.products);

        sb.append("?");

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("productType", mProductsBean.productType);

        LogUtils.e("-------------------------------"+mProductsBean.productType);


        if (mProductsBean.start == 0) {
            hashMap.put("start", mProductsBean.start);
        }


        OKHttpUtils.get(context, sb.toString(), hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mProductsBean = (ProductsBean) iBean;
    }
}
