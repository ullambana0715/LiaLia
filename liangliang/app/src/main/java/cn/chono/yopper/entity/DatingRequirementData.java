package cn.chono.yopper.entity;

import java.util.List;

/**
 * Created by SQ on 2016/1/7.
 */
public class DatingRequirementData {



    private String limitMsg;// 限制信息（只能发布5条有效约会哦）

    private List<DatingRequirment> requirements;

    public List<DatingRequirment> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<DatingRequirment> requirements) {
        this.requirements = requirements;
    }

    public String getLimitMsg() {
        return limitMsg;
    }

    public void setLimitMsg(String limitMsg) {
        this.limitMsg = limitMsg;
    }
}
