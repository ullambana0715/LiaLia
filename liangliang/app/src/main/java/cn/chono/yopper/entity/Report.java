package cn.chono.yopper.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16/7/26.
 */
public class Report implements Parcelable {

    public String message;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
    }

    public Report() {
    }

    protected Report(Parcel in) {
        this.message = in.readString();
    }

    public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
