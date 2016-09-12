package cn.chono.yopper.Service.Http.OrderLimits;

/**
 * Created by cc on 16/5/6.
 */
public class OrderLimitsEntity {

    public int result;// 投诉结果（0：成功 1：超出投诉有效期 2：重复投诉）

    public String msg;// 失败信息

}
