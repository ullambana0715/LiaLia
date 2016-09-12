package cn.chono.yopper.entity.likeBean;

import java.io.Serializable;

/**
 * Created by sunquan on 16/7/28.
 */
public class ILike implements Serializable {

    /**
     * id : 1
     * uid : sample string 2
     * mobile : sample string 3
     * hashedPassword : sample string 4
     * name : sample string 5
     * horoscope : 0
     * headImg : sample string 6
     * headImgInternal : sample string 7
     * sex : 0
     * status : 0
     * level : 0
     * regTime : 2016-07-25T09:56:22.1502887+08:00
     * cityCode : 8
     */

    private UserInfoBean userInfo;
    private boolean isUnlock;
    private boolean isActivityExpert;
    private int currentUserPosition;
    /**
     * activityId : sample string 1
     * activityName : sample string 2
     */

    private ActivityBean activity;
    /**
     * datingId : sample string 1
     * datingName : sample string 2
     */

    private DatingBean dating;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isIsUnlock() {
        return isUnlock;
    }

    public void setIsUnlock(boolean isUnlock) {
        this.isUnlock = isUnlock;
    }

    public boolean isIsActivityExpert() {
        return isActivityExpert;
    }

    public void setIsActivityExpert(boolean isActivityExpert) {
        this.isActivityExpert = isActivityExpert;
    }

    public int getCurrentUserPosition() {
        return currentUserPosition;
    }

    public void setCurrentUserPosition(int currentUserPosition) {
        this.currentUserPosition = currentUserPosition;
    }

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public DatingBean getDating() {
        return dating;
    }

    public void setDating(DatingBean dating) {
        this.dating = dating;
    }

    public static class UserInfoBean {
        private int id;
        private String uid;
        private String mobile;
        private String hashedPassword;
        private String name;
        private int horoscope;
        private String headImg;
        private String headImgInternal;
        private int sex;
        private int status;
        private int level;
        private String regTime;
        private int cityCode;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }

        public void setHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
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

        public String getHeadImgInternal() {
            return headImgInternal;
        }

        public void setHeadImgInternal(String headImgInternal) {
            this.headImgInternal = headImgInternal;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getRegTime() {
            return regTime;
        }

        public void setRegTime(String regTime) {
            this.regTime = regTime;
        }

        public int getCityCode() {
            return cityCode;
        }

        public void setCityCode(int cityCode) {
            this.cityCode = cityCode;
        }

        @Override
        public String toString() {
            return "UserInfoBean{" +
                    "id=" + id +
                    ", uid='" + uid + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", hashedPassword='" + hashedPassword + '\'' +
                    ", name='" + name + '\'' +
                    ", horoscope=" + horoscope +
                    ", headImg='" + headImg + '\'' +
                    ", headImgInternal='" + headImgInternal + '\'' +
                    ", sex=" + sex +
                    ", status=" + status +
                    ", level=" + level +
                    ", regTime='" + regTime + '\'' +
                    ", cityCode=" + cityCode +
                    '}';
        }
    }

    public static class ActivityBean {
        private String activityId;
        private String activityName;

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        @Override
        public String toString() {
            return "ActivityBean{" +
                    "activityId='" + activityId + '\'' +
                    ", activityName='" + activityName + '\'' +
                    '}';
        }
    }

    public static class DatingBean {
        private String datingId;
        private String datingName;

        public String getDatingId() {
            return datingId;
        }

        public void setDatingId(String datingId) {
            this.datingId = datingId;
        }

        public String getDatingName() {
            return datingName;
        }

        public void setDatingName(String datingName) {
            this.datingName = datingName;
        }

        @Override
        public String toString() {
            return "DatingBean{" +
                    "datingId='" + datingId + '\'' +
                    ", datingName='" + datingName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ILike{" +
                "userInfo=" + userInfo +
                ", isUnlock=" + isUnlock +
                ", isActivityExpert=" + isActivityExpert +
                ", currentUserPosition=" + currentUserPosition +
                ", activity=" + activity +
                ", dating=" + dating +
                '}';
    }
}
