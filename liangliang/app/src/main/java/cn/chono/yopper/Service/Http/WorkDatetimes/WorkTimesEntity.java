package cn.chono.yopper.Service.Http.WorkDatetimes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16/5/3.
 */
public class WorkTimesEntity implements Parcelable {

    public String workDatetimeId;// 编号

    public String workTime;// 工作日期

    public int orderCount; // 订单个数

    public boolean isFullReservation;// 是否预约已满


    public WorkTimesEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.workDatetimeId);
        dest.writeString(this.workTime);
        dest.writeInt(this.orderCount);
        dest.writeByte(isFullReservation ? (byte) 1 : (byte) 0);
    }

    protected WorkTimesEntity(Parcel in) {
        this.workDatetimeId = in.readString();
        this.workTime = in.readString();
        this.orderCount = in.readInt();
        this.isFullReservation = in.readByte() != 0;
    }

    public static final Creator<WorkTimesEntity> CREATOR = new Creator<WorkTimesEntity>() {
        @Override
        public WorkTimesEntity createFromParcel(Parcel source) {
            return new WorkTimesEntity(source);
        }

        @Override
        public WorkTimesEntity[] newArray(int size) {
            return new WorkTimesEntity[size];
        }
    };
}
