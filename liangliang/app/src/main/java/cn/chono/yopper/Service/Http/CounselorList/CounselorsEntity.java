package cn.chono.yopper.Service.Http.CounselorList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cc on 16/5/3.
 */
public class CounselorsEntity implements Parcelable {

    public String nextQuery;

    public List<CounselorsList> list;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nextQuery);
        dest.writeTypedList(list);
    }

    public CounselorsEntity() {
    }

    protected CounselorsEntity(Parcel in) {
        this.nextQuery = in.readString();
        this.list = in.createTypedArrayList(CounselorsList.CREATOR);
    }

    public static final Parcelable.Creator<CounselorsEntity> CREATOR = new Parcelable.Creator<CounselorsEntity>() {
        @Override
        public CounselorsEntity createFromParcel(Parcel source) {
            return new CounselorsEntity(source);
        }

        @Override
        public CounselorsEntity[] newArray(int size) {
            return new CounselorsEntity[size];
        }
    };
}
