package cn.chono.yopper.Service.Http.Products;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/5/5.
 */
public class ProductsBean extends ParameterBean {

    public int productType;//商品类型（0：苹果 1：星钻）

    public int start;//结果集偏移量，从0开始计算；默认为0
}
