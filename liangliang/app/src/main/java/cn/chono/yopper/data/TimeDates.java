package cn.chono.yopper.data;

/**
 * Created by cc on 16/3/28.
 */
public class TimeDates {

    private int days;

    private int month;

    private int Date;

    private String type;

    private boolean isData;

    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isData() {
        return isData;
    }

    public void setData(boolean data) {
        isData = data;
    }

    @Override
    public String toString() {
        return "TimeDates{" +
                "days=" + days +
                ", month=" + month +
                ", Date=" + Date +
                ", type='" + type + '\'' +
                '}';
    }
}
