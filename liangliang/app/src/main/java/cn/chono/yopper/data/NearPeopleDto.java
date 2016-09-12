package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by sunquan on 16/5/26.
 */
public class NearPeopleDto implements Serializable {

    private boolean isHot;


    // 是否活动达人 V2.5.4
    private boolean isActivityExpert;
    // V2.5.4  vip身份枚举  ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    private int currentUserPosition;

    private double distance;

    private boolean hasDating = false;

    private String headImg;

    private int horoscope;

    private int id;

    private int level;

    private String name;

    private int sex;

    private boolean showVisit = false;

    private String updateTime;

    private int visit;

    private boolean hasVideo = false;

    public boolean isHasVideo() {
        return this.hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(int horoscope) {
        this.horoscope = horoscope;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

    public boolean isHasDating() {
        return hasDating;
    }

    public void setHasDating(boolean hasDating) {
        this.hasDating = hasDating;
    }

    public boolean isShowVisit() {
        return showVisit;
    }

    public void setShowVisit(boolean showVisit) {
        this.showVisit = showVisit;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isActivityExpert() {
        return isActivityExpert;
    }

    public void setActivityExpert(boolean activityExpert) {
        isActivityExpert = activityExpert;
    }

    public int getCurrentUserPosition() {
        return currentUserPosition;
    }

    public void setCurrentUserPosition(int currentUserPosition) {
        this.currentUserPosition = currentUserPosition;
    }
}
