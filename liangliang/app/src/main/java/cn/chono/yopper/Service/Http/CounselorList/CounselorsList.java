package cn.chono.yopper.Service.Http.CounselorList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16/5/3.
 */
public class CounselorsList implements Parcelable {

    public int userId;// 用户编号

    public String avatar;// 头像地址

    public String nickName;// 昵称

    public String[] skillTags;// 擅长标签

    public double totalStarLevel;// 星级

    public  int totalAnswerCount;// 已解答次数

    public long charge;// 价格 收费标准（分）

    public String desc;// 简介

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CounselorsList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.avatar);
        dest.writeString(this.nickName);
        dest.writeStringArray(this.skillTags);
        dest.writeDouble(this.totalStarLevel);
        dest.writeInt(this.totalAnswerCount);
        dest.writeLong(this.charge);
    }

    protected CounselorsList(Parcel in) {
        this.userId = in.readInt();
        this.avatar = in.readString();
        this.nickName = in.readString();
        this.skillTags = in.createStringArray();
        this.totalStarLevel = in.readDouble();
        this.totalAnswerCount = in.readInt();
        this.charge = in.readLong();
    }

    public static final Creator<CounselorsList> CREATOR = new Creator<CounselorsList>() {
        @Override
        public CounselorsList createFromParcel(Parcel source) {
            return new CounselorsList(source);
        }

        @Override
        public CounselorsList[] newArray(int size) {
            return new CounselorsList[size];
        }
    };
}
