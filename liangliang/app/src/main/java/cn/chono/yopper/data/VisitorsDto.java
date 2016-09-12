package cn.chono.yopper.data;

public class VisitorsDto {


    private String lastVisitTime;

    private boolean isNew;

    // V2.5.4  vip身份 ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    private int currentUserPosition;
    private User user;

    public String getLastVisitTime() {
        return lastVisitTime;
    }


    public void setLastVisitTime(String lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }


    public boolean isNew() {
        return isNew;
    }


    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public int getCurrentUserPosition() {
        return currentUserPosition;
    }

    public void setCurrentUserPosition(int currentUserPosition) {
        this.currentUserPosition = currentUserPosition;
    }
    

    public class User {

        private int id;

        private String mobile;

        private String hashedPassword;

        private String name;

        private int horoscope;

        private String headImg;

        private String headImgInternal;

        private int sex;

        private int status;

        private String regTime;

        private int cityCode;


        public String getHeadImgInternal() {
            return headImgInternal;
        }

        public void setHeadImgInternal(String headImgInternal) {
            this.headImgInternal = headImgInternal;
        }

        public int getCityCode() {
            return cityCode;
        }

        public void setCityCode(int cityCode) {
            this.cityCode = cityCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getRegTime() {
            return regTime;
        }

        public void setRegTime(String regTime) {
            this.regTime = regTime;
        }

    }


}
