package cn.chono.yopper.Service.Http.VipConfigs;

import java.util.List;

import cn.chono.yopper.data.ApplesEntity;
import cn.chono.yopper.data.VipRenewalsPrivilegeEntity;

/**
 * Created by cc on 16/6/14.
 */
public class VipConfigsListEntity {

    /**
     * userPosition : 0
     * imgUrl : sample string 1
     * dayCount : 2
     * appleCount : 3
     * vipRight : ["sample string 1","sample string 2","sample string 3"]
     */

    public int userPosition;// V2.5.4 vip身份类型 ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    public String vipName; // vip 名字
    public int dayCount; // 时长天数
    public int appleCount;// 消耗苹果数
    public List<String> vipRight; // 权益


    public VipRenewalsPrivilegeEntity mVipRenewalsPrivilegeEntity ;//VIP的介绍

    public ApplesEntity mApplesEntity;//苹果数量

    public String mVIPTITLE;

}
