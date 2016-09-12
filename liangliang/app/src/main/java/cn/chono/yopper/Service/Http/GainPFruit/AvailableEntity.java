package cn.chono.yopper.Service.Http.GainPFruit;

/**
 * Created by cc on 16/6/14.
 */
public class AvailableEntity {


    /**
     * availableBalance : 1
     * keyCount : 2
     * keyPrice : 3
     * vipPostion : {"userPosition":0,"validDate":"2016-06-14T17:05:35.9513099+08:00","validStatus":1,"vipRightList":[{"isNeedLookStore":true,"description":"sample string 2"},{"isNeedLookStore":true,"description":"sample string 2"},{"isNeedLookStore":true,"description":"sample string 2"}]}
     */

    public int availableBalance;// 可用P果总数
    public int keyCount;// 钥匙总数
    public int keyPrice;// 钥匙单价（单位：苹果/把）

    // 剩余魅力值
    public int remainCharm;
    /**
     * userPosition : 0
     * validDate : 2016-06-14T17:05:35.9513099+08:00
     * validStatus : 1
     * vipRightList : [{"isNeedLookStore":true,"description":"sample string 2"},{"isNeedLookStore":true,"description":"sample string 2"},{"isNeedLookStore":true,"description":"sample string 2"}]
     */

    public VipPostionEntity vipPostion;


}
