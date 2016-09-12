package cn.chono.yopper.Service.Http.EvaluationList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationsEntity implements Parcelable {

    public String nextQuery;

    public int start;

    public List<EvaluationsListEntity> list;


    public EvaluationsEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nextQuery);
        dest.writeInt(this.start);
        dest.writeTypedList(list);
    }

    protected EvaluationsEntity(Parcel in) {
        this.nextQuery = in.readString();
        this.start = in.readInt();
        this.list = in.createTypedArrayList(EvaluationsListEntity.CREATOR);
    }

    public static final Creator<EvaluationsEntity> CREATOR = new Creator<EvaluationsEntity>() {
        @Override
        public EvaluationsEntity createFromParcel(Parcel source) {
            return new EvaluationsEntity(source);
        }

        @Override
        public EvaluationsEntity[] newArray(int size) {
            return new EvaluationsEntity[size];
        }
    };
}
