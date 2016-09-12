package cn.chono.yopper.Service.Http.EvaluationList;

import android.os.Parcel;
import android.os.Parcelable;

import cn.chono.yopper.Service.Http.RespBean;

/**
 * Created by cc on 16/5/3.
 */
public class EvaluationListRespEntity extends RespBean implements Parcelable {

    public EvaluationDataEntity resp;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resp, flags);
    }

    public EvaluationListRespEntity() {
    }

    protected EvaluationListRespEntity(Parcel in) {
        this.resp = in.readParcelable(EvaluationDataEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<EvaluationListRespEntity> CREATOR = new Parcelable.Creator<EvaluationListRespEntity>() {
        @Override
        public EvaluationListRespEntity createFromParcel(Parcel source) {
            return new EvaluationListRespEntity(source);
        }

        @Override
        public EvaluationListRespEntity[] newArray(int size) {
            return new EvaluationListRespEntity[size];
        }
    };
}
