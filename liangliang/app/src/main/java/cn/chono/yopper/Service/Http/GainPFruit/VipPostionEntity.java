package cn.chono.yopper.Service.Http.GainPFruit;

import java.util.List;

/**
 * Created by cc on 16/6/14.
 */
public class VipPostionEntity {


    /**
     * userPosition : 0
     * validDate : 2016-06-14T17:05:35.9513099+08:00
     * validStatus : 1
     * vipRightList : [{"isNeedLookStore":true,"description":"sample string 2"},{"isNeedLookStore":true,"description":"sample string 2"},{"isNeedLookStore":true,"description":"sample string 2"}]
     */

    public String title;

    public int lastUserVipPosition;// V2.5.4 vip身份 ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    public String validDate; // V2.5.4 有效日期
    public int validStatus;// V2.5.4  过期状态 1，表示有效期 2表示即将过期 3 表示过期不可用
    /**
     * isNeedLookStore : true
     * description : sample string 2
     */

    public List<VipPostionListEntity> vipRightList;// V2.5.4 权益列表


}
