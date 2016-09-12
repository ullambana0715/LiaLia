package cn.chono.yopper.Service.Http.DatingPublish;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 邀约吃饭
 * Created by cc on 16/3/31.
 */
public class Dine implements Parcelable {


    private int costType;// 买单（0：我买单 1：AA 2：你买单）（必填）

    private int companionCondition;// 同伴情况（0：不携带 1：可携带一名闺蜜 2：可携带三两好友）（必填）

    private String firstArea;// 一级地区（上海、浙江）

    private String secondArea;// 二级地区（静安、杭州）

    private String address;// 地址

    private String meetingTime;// 邀约时间

    private int meetingTimeType;// 邀约时间类型（0：不限时间 1：平时周末 2：选择时间）（必填）

    private int targetSex;// 对象性别（0：不限 1：男 2：女）（必填）

    private int carry;// 接送类型（0：不用接送 1：我接送 2：需接送我）（必填）

    private String description;// // 描述（必填 少于200字

    private String photoUrl;// 照片Url

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public int getCompanionCondition() {
        return companionCondition;
    }

    public void setCompanionCondition(int companionCondition) {
        this.companionCondition = companionCondition;
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
        return "Dine{" +
                "costType=" + costType +
                ", companionCondition=" + companionCondition +
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
        dest.writeInt(this.costType);
        dest.writeInt(this.companionCondition);
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

    public Dine() {
    }

    protected Dine(Parcel in) {
        this.costType = in.readInt();
        this.companionCondition = in.readInt();
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

    public static final Parcelable.Creator<Dine> CREATOR = new Parcelable.Creator<Dine>() {
        @Override
        public Dine createFromParcel(Parcel source) {
            return new Dine(source);
        }

        @Override
        public Dine[] newArray(int size) {
            return new Dine[size];
        }
    };
}
