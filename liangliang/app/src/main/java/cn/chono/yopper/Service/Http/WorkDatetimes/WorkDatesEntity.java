package cn.chono.yopper.Service.Http.WorkDatetimes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cc on 16/5/3.
 */
public class WorkDatesEntity implements Parcelable {


    public String workDate;// 工作日期

    public List<WorkTimesEntity> workTimes;//时间列表


    public WorkDatesEntity() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.workDate);
        dest.writeTypedList(workTimes);
    }

    protected WorkDatesEntity(Parcel in) {
        this.workDate = in.readString();
        this.workTimes = in.createTypedArrayList(WorkTimesEntity.CREATOR);
    }

    public static final Creator<WorkDatesEntity> CREATOR = new Creator<WorkDatesEntity>() {
        @Override
        public WorkDatesEntity createFromParcel(Parcel source) {
            return new WorkDatesEntity(source);
        }

        @Override
        public WorkDatesEntity[] newArray(int size) {
            return new WorkDatesEntity[size];
        }
    };
}
