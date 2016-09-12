package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/6/14.
 */
public class IndexActivities implements Serializable {

    /**
     * activityId : sample string 1
     * title : sample string 2
     * titleImageUrl : sample string 3
     * activityStartTime : 2016-06-14T16:50:34.9557759+08:00
     * city : sample string 5
     * fee : 6
     * address : sample string 7
     * joinEndTime : 2016-06-14T16:50:34.956776+08:00
     * isEnd : true
     */

    // 报名状态（0：未报名 1：报名成功 2：报名已退款 3：已完成）
    private int joinStatus;
    private String activityId;
    private String title;
    private String titleImageUrl;
    private String activityStartTime;
    private String city;
    private int fee;
    private String address;
    private String joinEndTime;
    // 是否允许会员使用免费参与机会
    private boolean allowFreeByMember;
    // 活动状态（0：正常 1：人数已满 2：报名已截止 3：已取消 4：已结束）
    private int activityStatus;

    public int getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(int joinStatus) {
        this.joinStatus = joinStatus;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImageUrl() {
        return titleImageUrl;
    }

    public void setTitleImageUrl(String titleImageUrl) {
        this.titleImageUrl = titleImageUrl;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoinEndTime() {
        return joinEndTime;
    }

    public void setJoinEndTime(String joinEndTime) {
        this.joinEndTime = joinEndTime;
    }

    public boolean isAllowFreeByMember() {
        return allowFreeByMember;
    }

    public void setAllowFreeByMember(boolean allowFreeByMember) {
        this.allowFreeByMember = allowFreeByMember;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    @Override
    public String toString() {
        return "IndexActivities{" +
                "joinStatus=" + joinStatus +
                ", activityId='" + activityId + '\'' +
                ", title='" + title + '\'' +
                ", titleImageUrl='" + titleImageUrl + '\'' +
                ", activityStartTime='" + activityStartTime + '\'' +
                ", city='" + city + '\'' +
                ", fee=" + fee +
                ", address='" + address + '\'' +
                ", joinEndTime='" + joinEndTime + '\'' +
                ", allowFreeByMember=" + allowFreeByMember +
                ", activityStatus=" + activityStatus +
                '}';
    }
}
