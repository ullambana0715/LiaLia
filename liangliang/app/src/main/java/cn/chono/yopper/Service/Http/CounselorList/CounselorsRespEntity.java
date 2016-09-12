package cn.chono.yopper.Service.Http.CounselorList;

import android.os.Parcel;
import android.os.Parcelable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/5/3.
 */
public class CounselorsRespEntity extends RespBean implements Parcelable {

    public CounselorsEntity resp;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resp, flags);
    }

    public CounselorsRespEntity() {
    }

    protected CounselorsRespEntity(Parcel in) {
        this.resp = in.readParcelable(CounselorsEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<CounselorsRespEntity> CREATOR = new Parcelable.Creator<CounselorsRespEntity>() {
        @Override
        public CounselorsRespEntity createFromParcel(Parcel source) {
            return new CounselorsRespEntity(source);
        }

        @Override
        public CounselorsRespEntity[] newArray(int size) {
            return new CounselorsRespEntity[size];
        }
    };
}
