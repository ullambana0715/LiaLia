package cn.chono.yopper.Service.Http.PublishDating;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class PublishDatingBean extends ParameterBean {


    private int datingType;
    private int locationId;
    private int inviteeId;
    private String meetingDate;

    public int getDatingType() {
        return datingType;
    }

    public void setDatingType(int datingType) {
        this.datingType = datingType;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(int inviteeId) {
        this.inviteeId = inviteeId;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }
}
