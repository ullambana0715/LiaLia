package cn.chono.yopper.Service.Http.DatingInvite;

import cn.chono.yopper.Service.Http.DatingPublish.Dine;
import cn.chono.yopper.Service.Http.DatingPublish.Marriage;
import cn.chono.yopper.Service.Http.DatingPublish.Movie;
import cn.chono.yopper.Service.Http.DatingPublish.Other;
import cn.chono.yopper.Service.Http.DatingPublish.Singing;
import cn.chono.yopper.Service.Http.DatingPublish.Sports;
import cn.chono.yopper.Service.Http.DatingPublish.Travel;
import cn.chono.yopper.Service.Http.DatingPublish.WalkTheDog;
import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/3/31.
 */
public class DatingsInviteBean extends ParameterBean {

    private String dateId;//此字段可选。用于修改邀约


    private String targetUserId;


    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }


    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }
}
