package cn.chono.yopper.Service.Http.ProductsMore;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.Products.ProductsRespEntity;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by cc on 16/5/5.
 */
public class ProductsMoreService extends HttpService {

    public ProductsMoreService(Context context) {
        super(context);
    }

    ProductsMoreBean mProductsMoreBean;

    @Override
    public void enqueue() {

        OutDataClass = ProductsRespEntity.class;

        OKHttpUtils.get(context, mProductsMoreBean.nextQuery, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mProductsMoreBean = (ProductsMoreBean) iBean;
    }
}
