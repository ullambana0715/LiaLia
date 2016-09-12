package cn.chono.yopper.Service.Http.VipConfigs;

import java.util.List;

/**
 * Created by cc on 16/6/14.
 */
public class VipConfigsEntity {


    /**
     * rows : 1
     * start : 2
     * total : 3
     * list : [{"userPosition":0,"imgUrl":"sample string 1","dayCount":2,"appleCount":3,"vipRight":["sample string 1","sample string 2","sample string 3"]},{"userPosition":0,"imgUrl":"sample string 1","dayCount":2,"appleCount":3,"vipRight":["sample string 1","sample string 2","sample string 3"]},{"userPosition":0,"imgUrl":"sample string 1","dayCount":2,"appleCount":3,"vipRight":["sample string 1","sample string 2","sample string 3"]}]
     */

    public int rows;
    public int start;
    public int total;
    /**
     * userPosition : 0
     * imgUrl : sample string 1
     * dayCount : 2
     * appleCount : 3
     * vipRight : ["sample string 1","sample string 2","sample string 3"]
     */

    public List<VipConfigsListEntity> list;




}
