package cn.chono.yopper.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16/7/26.
 */
public class HeadEntity implements Parcelable {

    public String headImgUrl;

    public String albumImgUrl;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headImgUrl);
        dest.writeString(this.albumImgUrl);
    }

    public HeadEntity() {
    }

    protected HeadEntity(Parcel in) {
        this.headImgUrl = in.readString();
        this.albumImgUrl = in.readString();
    }

    public static final Parcelable.Creator<HeadEntity> CREATOR = new Parcelable.Creator<HeadEntity>() {
        @Override
        public HeadEntity createFromParcel(Parcel source) {
            return new HeadEntity(source);
        }

        @Override
        public HeadEntity[] newArray(int size) {
            return new HeadEntity[size];
        }
    };

    @Override
    public String toString() {
        return "HeadEntity{" +
                "headImgUrl='" + headImgUrl + '\'' +
                ", albumImgUrl='" + albumImgUrl + '\'' +
                '}';
    }
}
