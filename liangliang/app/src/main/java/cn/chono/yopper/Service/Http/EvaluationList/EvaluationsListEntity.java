package cn.chono.yopper.Service.Http.EvaluationList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationsListEntity implements Parcelable {

    public EvaluatiosUserEntity user;

    public long  stars;

    public String[] tags;

    public String description;

    public String createTime;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.stars);
        dest.writeStringArray(this.tags);
        dest.writeString(this.description);
        dest.writeString(this.createTime);
    }

    public EvaluationsListEntity() {
    }

    protected EvaluationsListEntity(Parcel in) {
        this.user = in.readParcelable(EvaluatiosUserEntity.class.getClassLoader());
        this.stars = in.readLong();
        this.tags = in.createStringArray();
        this.description = in.readString();
        this.createTime = in.readString();
    }

    public static final Parcelable.Creator<EvaluationsListEntity> CREATOR = new Parcelable.Creator<EvaluationsListEntity>() {
        @Override
        public EvaluationsListEntity createFromParcel(Parcel source) {
            return new EvaluationsListEntity(source);
        }

        @Override
        public EvaluationsListEntity[] newArray(int size) {
            return new EvaluationsListEntity[size];
        }
    };
}
