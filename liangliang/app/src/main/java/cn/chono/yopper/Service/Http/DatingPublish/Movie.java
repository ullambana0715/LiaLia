package cn.chono.yopper.Service.Http.DatingPublish;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 邀约看电影
 * Created by cc on 16/3/31.
 */
public class Movie implements Parcelable {

    private String name;// 影片名称（必填）

    private int costType;// 买单（0：我买单 1：AA 2：你买单）（必填）

    private String firstArea;// 一级地区（上海、浙江）

    private String secondArea;// 二级地区（静安、杭州）

    private String address;// 地址

    private String meetingTime;// 邀约时间

    private int meetingTimeType;// 邀约时间类型（0：不限时间 1：平时周末 2：选择时间）（必填）

    private int targetSex;// 对象性别（0：不限 1：男 2：女）（必填）

    private int carry;// 接送类型（0：不用接送 1：我接送 2：需接送我）（必填）

    private String description;// 描述（必填 少于200字）

    private String photoUrl;// 照片Url

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public int getMeetingTimeType() {
        return meetingTimeType;
    }

    public void setMeetingTimeType(int meetingTimeType) {
        this.meetingTimeType = meetingTimeType;
    }

    public int getTargetSex() {
        return targetSex;
    }

    public void setTargetSex(int targetSex) {
        this.targetSex = targetSex;
    }

    public int getCarry() {
        return carry;
    }

    public void setCarry(int carry) {
        this.carry = carry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", costType=" + costType +
                ", firstArea='" + firstArea + '\'' +
                ", secondArea='" + secondArea + '\'' +
                ", address='" + address + '\'' +
                ", meetingTime='" + meetingTime + '\'' +
                ", meetingTimeType=" + meetingTimeType +
                ", targetSex=" + targetSex +
                ", carry=" + carry +
                ", description='" + description + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.costType);
        dest.writeString(this.firstArea);
        dest.writeString(this.secondArea);
        dest.writeString(this.address);
        dest.writeString(this.meetingTime);
        dest.writeInt(this.meetingTimeType);
        dest.writeInt(this.targetSex);
        dest.writeInt(this.carry);
        dest.writeString(this.description);
        dest.writeString(this.photoUrl);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.name = in.readString();
        this.costType = in.readInt();
        this.firstArea = in.readString();
        this.secondArea = in.readString();
        this.address = in.readString();
        this.meetingTime = in.readString();
        this.meetingTimeType = in.readInt();
        this.targetSex = in.readInt();
        this.carry = in.readInt();
        this.description = in.readString();
        this.photoUrl = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
