package cn.chono.yopper.data;

import java.io.Serializable;

public class AppointOwner implements Serializable {

    private static final transient long serialVersionUID = 1L;

    private int userId;

    private String name;

    private String headImg;

    private int sex;

    private int age;

    private boolean birthdayPrivacy;

    private boolean isHot;

    // 是否活动达人 V2.5.4
    private boolean isActivityExpert;

    // V2.5.4  vip身份 ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    private int currentUserPosition;

    private boolean isVideoVerification;

    private int sincerity;

    private int horoscope;

    private int level;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isVideoVerification() {
        return isVideoVerification;
    }

    public void setVideoVerification(boolean videoVerification) {
        isVideoVerification = videoVerification;
    }

    public int getSincerity() {
        return sincerity;
    }

    public void setSincerity(int sincerity) {
        this.sincerity = sincerity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(int horoscope) {
        this.horoscope = horoscope;
    }

    public boolean isBirthdayPrivacy() {
        return birthdayPrivacy;
    }

    public void setBirthdayPrivacy(boolean birthdayPrivacy) {
        this.birthdayPrivacy = birthdayPrivacy;
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
