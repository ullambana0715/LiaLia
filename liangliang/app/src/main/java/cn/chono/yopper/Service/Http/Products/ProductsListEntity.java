package cn.chono.yopper.Service.Http.Products;

/**
 * Created by cc on 16/5/5.
 */
public class ProductsListEntity {

    public String productId;// 商品Id

    public String productName;// 商品名称

    public String description;// 商品描述

    public boolean isHighlight;// 是否高亮

    public long price; // 商品价格（单位：分）

    public String imageUrl;// 商品图片Url

    public int count;// 商品数量

    public int giveCount; // 赠送数量
}
