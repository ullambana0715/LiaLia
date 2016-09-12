package cn.chono.yopper.Service.Http.EvaluationList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationsAggregationEntity implements Parcelable {

    public int total;

    public double avgStars;

    public List<EvaluationTagsEntity> tags;


    public EvaluationsAggregationEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeDouble(this.avgStars);
        dest.writeList(this.tags);
    }

    protected EvaluationsAggregationEntity(Parcel in) {
        this.total = in.readInt();
        this.avgStars = in.readDouble();
        this.tags = new ArrayList<EvaluationTagsEntity>();
        in.readList(this.tags, EvaluationTagsEntity.class.getClassLoader());
    }

    public static final Creator<EvaluationsAggregationEntity> CREATOR = new Creator<EvaluationsAggregationEntity>() {
        @Override
        public EvaluationsAggregationEntity createFromParcel(Parcel source) {
            return new EvaluationsAggregationEntity(source);
        }

        @Override
        public EvaluationsAggregationEntity[] newArray(int size) {
            return new EvaluationsAggregationEntity[size];
        }
    };
}
