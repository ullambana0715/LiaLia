package cn.chono.yopper.Service.Http.WorkDatetimes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cc on 16/5/3.
 */
public class WorkDateEntity implements Parcelable {

    public int userId;// 用户编号

    public List<WorkDatesEntity> workDatetimes;// 日期列表


    public WorkDateEntity() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeTypedList(this.workDatetimes);
    }

    protected WorkDateEntity(Parcel in) {
        this.userId = in.readInt();
        this.workDatetimes = in.createTypedArrayList(WorkDatesEntity.CREATOR);
    }

    public static final Creator<WorkDateEntity> CREATOR = new Creator<WorkDateEntity>() {
        @Override
        public WorkDateEntity createFromParcel(Parcel source) {
            return new WorkDateEntity(source);
        }

        @Override
        public WorkDateEntity[] newArray(int size) {
            return new WorkDateEntity[size];
        }
    };
}
