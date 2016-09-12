package cn.chono.yopper.Service.Http.DatingPublish;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by cc on 16/3/31.
 */
public class DatingsBean extends ParameterBean {

    private String datingId;//此字段可选。用于修改邀约

    private double lat;// 邀约发布时的发布者的纬度

    private double lng;// 邀约发布时的发布者的经度

    private String firstArea;// 一级地区（上海、浙江）

    private String secondArea;// 二级地区（静安、杭州）

    private int activityType;// 活动类型（1：征婚 2：旅行 3：吃饭 4：看电影 5：运动健身 6：遛狗 7：唱歌 8：其他）

    private Marriage marriage;// 征婚

    private Travel travel;// 邀约旅行

    private Dine dine;// 邀约吃饭

    private Movie movie;// 邀约看电影

    private Sports sports;// 邀约运动健身

    private WalkTheDog walkTheDog;// 邀约遛狗

    private Singing singing;// 邀约唱歌

    private Other other;// 邀约其他


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public Marriage getMarriage() {
        return marriage;
    }

    public void setMarriage(Marriage marriage) {
        this.marriage = marriage;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public Dine getDine() {
        return dine;
    }

    public void setDine(Dine dine) {
        this.dine = dine;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Sports getSports() {
        return sports;
    }

    public void setSports(Sports sports) {
        this.sports = sports;
    }

    public WalkTheDog getWalkTheDog() {
        return walkTheDog;
    }

    public void setWalkTheDog(WalkTheDog walkTheDog) {
        this.walkTheDog = walkTheDog;
    }

    public Singing getSinging() {
        return singing;
    }

    public void setSinging(Singing singing) {
        this.singing = singing;
    }

    public Other getOther() {
        return other;
    }

    public void setOther(Other other) {
        this.other = other;
    }

    public String getFirstArea() {
        return firstArea;
    }

    public void setFirstArea(String firstArea) {
        this.firstArea = firstArea;
    }

    public String getSecondArea() {
        return secondArea;
    }

    public void setSecondArea(String secondArea) {
        this.secondArea = secondArea;
    }

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    @Override
    public String toString() {
        return "DatingsBean{" +
                "datingId='" + datingId + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", firstArea='" + firstArea + '\'' +
                ", secondArea='" + secondArea + '\'' +
                ", activityType=" + activityType +
                ", marriage=" + marriage +
                ", travel=" + travel +
                ", dine=" + dine +
                ", movie=" + movie +
                ", sports=" + sports +
                ", walkTheDog=" + walkTheDog +
                ", singing=" + singing +
                ", other=" + other +
                '}';
    }
}
