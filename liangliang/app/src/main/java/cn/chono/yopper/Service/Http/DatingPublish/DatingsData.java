package cn.chono.yopper.Service.Http.DatingPublish;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.entity.DatingRequirment;

/**
 * Created by cc on 16/4/6.
 */
public class DatingsData implements Parcelable {

    private boolean isSuccess;

    private String limitMsg;

    private List<DatingRequirment> requirements;


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getLimitMsg() {
        return limitMsg;
    }

    public void setLimitMsg(String limitMsg) {
        this.limitMsg = limitMsg;
    }

    public List<DatingRequirment> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<DatingRequirment> requirements) {
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return "DatingsData{" +
                "isSuccess=" + isSuccess +
                ", limitMsg='" + limitMsg + '\'' +
                ", requirements=" + requirements +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isSuccess ? (byte) 1 : (byte) 0);
        dest.writeString(this.limitMsg);
        dest.writeList(this.requirements);
    }

    public DatingsData() {
    }

    protected DatingsData(Parcel in) {
        this.isSuccess = in.readByte() != 0;
        this.limitMsg = in.readString();
        this.requirements = new ArrayList<DatingRequirment>();
        in.readList(this.requirements, DatingRequirment.class.getClassLoader());
    }

    public static final Parcelable.Creator<DatingsData> CREATOR = new Parcelable.Creator<DatingsData>() {
        @Override
        public DatingsData createFromParcel(Parcel source) {
            return new DatingsData(source);
        }

        @Override
        public DatingsData[] newArray(int size) {
            return new DatingsData[size];
        }
    };
}
