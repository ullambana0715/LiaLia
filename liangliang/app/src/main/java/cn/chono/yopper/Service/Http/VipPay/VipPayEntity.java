package cn.chono.yopper.Service.Http.VipPay;

/**
 * Created by cc on 16/6/14.
 */
public class VipPayEntity {

    public int buyVip;// 0表示正常，1表示购买苹果不足

    public String message;// 如果BuyVip 为1的时候，会有提示信息

    public int appleCount; // 苹果个数
}
