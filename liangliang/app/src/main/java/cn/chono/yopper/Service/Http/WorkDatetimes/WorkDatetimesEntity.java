package cn.chono.yopper.Service.Http.WorkDatetimes;

import android.os.Parcel;
import android.os.Parcelable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/5/3.
 */
public class WorkDatetimesEntity extends RespBean implements Parcelable {

   public WorkDateEntity resp;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resp, flags);
    }

    public WorkDatetimesEntity() {
    }

    protected WorkDatetimesEntity(Parcel in) {
        this.resp = in.readParcelable(WorkDateEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<WorkDatetimesEntity> CREATOR = new Parcelable.Creator<WorkDatetimesEntity>() {
        @Override
        public WorkDatetimesEntity createFromParcel(Parcel source) {
            return new WorkDatetimesEntity(source);
        }

        @Override
        public WorkDatetimesEntity[] newArray(int size) {
            return new WorkDatetimesEntity[size];
        }
    };
}
