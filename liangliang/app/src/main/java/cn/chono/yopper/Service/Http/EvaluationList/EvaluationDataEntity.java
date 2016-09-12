package cn.chono.yopper.Service.Http.EvaluationList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationDataEntity implements Parcelable {

    public EvaluationsAggregationEntity evaluationsAggregation;

    public EvaluationsEntity evaluations;


    public EvaluationDataEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.evaluationsAggregation, flags);
        dest.writeParcelable(this.evaluations, flags);
    }

    protected EvaluationDataEntity(Parcel in) {
        this.evaluationsAggregation = in.readParcelable(EvaluationsAggregationEntity.class.getClassLoader());
        this.evaluations = in.readParcelable(EvaluationsEntity.class.getClassLoader());
    }

    public static final Creator<EvaluationDataEntity> CREATOR = new Creator<EvaluationDataEntity>() {
        @Override
        public EvaluationDataEntity createFromParcel(Parcel source) {
            return new EvaluationDataEntity(source);
        }

        @Override
        public EvaluationDataEntity[] newArray(int size) {
            return new EvaluationDataEntity[size];
        }
    };
}
