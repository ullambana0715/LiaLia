package cn.chono.yopper.Service.Http.DatingPublish;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * 邀约旅行
 * Created by cc on 16/3/31.
 */
public class Travel implements Parcelable {

    private String[] wishTags;// 希望与偏好标签集合（少玩景点、享受慵懒假期......）

    private int targetObject;// 目标对象（男形象：1：土豪男 2：霸道总裁 3：高富帅 女形象：4：秀外慧中气质女 5：傻白甜 6：白富美）

    private int planTime;// 预计时间（0：当天往返 1：一两天 2：两三天 3：三五天 4：十天半月 5：还准备回来吗？ 6：会不会回不来？）

    private int method;// 出行方式（0：往返双飞 1：动车高铁 2：游轮 3：自驾 4：大巴 5：骑行）

    private int travelCostType;//男性费用（0：我承担全部 1：AA 2：来回机票我承担 3：承担对方2000以内的费用 4：承担对方3000以内的费用 5：承担对方5000以内的费用）（必填）
    //           女性费用（0：我承担全部 1：AA 2：希望男方买单 3：你丑你买单 4：我胸大你承担 5：剪刀石头布）（必填）

    private String photoUrl;// 照片Url

    private String firstArea;// 一级地区（上海、浙江）

    private String secondArea;// 二级地区（静安、杭州）

    private String address;// 地址

    private String[] meaningTags;// 旅行的意义标签集合（说走就走的旅行、亲近自然......）

    private String meetingTime;// 邀约时间

    private int meetingTravelTimeType;// 邀约旅行时间（0：近期出发 1：某个周末 2：具体日期 3：可商议 4：说走就走）

    private String description;// 描述

    @Override
    public String toString() {
        return "Travel{" +
                "wishTags=" + Arrays.toString(wishTags) +
                ", targetObject=" + targetObject +
                ", planTime=" + planTime +
                ", method=" + method +
                ", travelCostType=" + travelCostType +
                ", photoUrl='" + photoUrl + '\'' +
                ", firstArea='" + firstArea + '\'' +
                ", secondArea='" + secondArea + '\'' +
                ", address='" + address + '\'' +
                ", meaningTags=" + Arrays.toString(meaningTags) +
                ", meetingTime='" + meetingTime + '\'' +
                ", meetingTravelTimeType=" + meetingTravelTimeType +
                ", description='" + description + '\'' +
                '}';
    }

    public String[] getWishTags() {
        return wishTags;
    }

    public void setWishTags(String[] wishTags) {
        this.wishTags = wishTags;
    }

    public int getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(int targetObject) {
        this.targetObject = targetObject;
    }

    public int getPlanTime() {
        return planTime;
    }

    public void setPlanTime(int planTime) {
        this.planTime = planTime;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getTravelCostType() {
        return travelCostType;
    }

    public void setTravelCostType(int travelCostType) {
        this.travelCostType = travelCostType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public String[] getMeaningTags() {
        return meaningTags;
    }

    public void setMeaningTags(String[] meaningTags) {
        this.meaningTags = meaningTags;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public int getMeetingTravelTimeType() {
        return meetingTravelTimeType;
    }

    public void setMeetingTravelTimeType(int meetingTravelTimeType) {
        this.meetingTravelTimeType = meetingTravelTimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.wishTags);
        dest.writeInt(this.targetObject);
        dest.writeInt(this.planTime);
        dest.writeInt(this.method);
        dest.writeInt(this.travelCostType);
        dest.writeString(this.photoUrl);
        dest.writeString(this.firstArea);
        dest.writeString(this.secondArea);
        dest.writeString(this.address);
        dest.writeStringArray(this.meaningTags);
        dest.writeString(this.meetingTime);
        dest.writeInt(this.meetingTravelTimeType);
        dest.writeString(this.description);
    }

    public Travel() {
    }

    protected Travel(Parcel in) {
        this.wishTags = in.createStringArray();
        this.targetObject = in.readInt();
        this.planTime = in.readInt();
        this.method = in.readInt();
        this.travelCostType = in.readInt();
        this.photoUrl = in.readString();
        this.firstArea = in.readString();
        this.secondArea = in.readString();
        this.address = in.readString();
        this.meaningTags = in.createStringArray();
        this.meetingTime = in.readString();
        this.meetingTravelTimeType = in.readInt();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Travel> CREATOR = new Parcelable.Creator<Travel>() {
        @Override
        public Travel createFromParcel(Parcel source) {
            return new Travel(source);
        }

        @Override
        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };
}
