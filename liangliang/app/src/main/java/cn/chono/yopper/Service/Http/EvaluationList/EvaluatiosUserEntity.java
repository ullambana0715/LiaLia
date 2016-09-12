package cn.chono.yopper.Service.Http.EvaluationList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluatiosUserEntity implements Parcelable {

    public int userId;

    public String name;

    public String headImg;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.headImg);
    }

    public EvaluatiosUserEntity() {
    }

    protected EvaluatiosUserEntity(Parcel in) {
        this.userId = in.readInt();
        this.name = in.readString();
        this.headImg = in.readString();
    }

    public static final Parcelable.Creator<EvaluatiosUserEntity> CREATOR = new Parcelable.Creator<EvaluatiosUserEntity>() {
        @Override
        public EvaluatiosUserEntity createFromParcel(Parcel source) {
            return new EvaluatiosUserEntity(source);
        }

        @Override
        public EvaluatiosUserEntity[] newArray(int size) {
            return new EvaluatiosUserEntity[size];
        }
    };
}
