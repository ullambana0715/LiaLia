package cn.chono.yopper.event;

/**
 * Created by sunquan on 16/5/31.
 */
public class DatingsFilterEvent {

    private int appointtype;

    private int sextype;

    private int sorttype;

    private String firstArea_str;

    private String secondArea_str;


    public DatingsFilterEvent() {
    }

    public DatingsFilterEvent(int appointtype, int sextype, int sorttype, String firstArea_str, String secondArea_str) {
        this.appointtype = appointtype;
        this.sextype = sextype;
        this.sorttype = sorttype;
        this.firstArea_str = firstArea_str;
        this.secondArea_str = secondArea_str;
    }

    public int getAppointtype() {
        return appointtype;
    }

    public void setAppointtype(int appointtype) {
        this.appointtype = appointtype;
    }

    public int getSextype() {
        return sextype;
    }

    public void setSextype(int sextype) {
        this.sextype = sextype;
    }

    public int getSorttype() {
        return sorttype;
    }

    public void setSorttype(int sorttype) {
        this.sorttype = sorttype;
    }

    public String getFirstArea_str() {
        return firstArea_str;
    }

    public void setFirstArea_str(String firstArea_str) {
        this.firstArea_str = firstArea_str;
    }

    public String getSecondArea_str() {
        return secondArea_str;
    }

    public void setSecondArea_str(String secondArea_str) {
        this.secondArea_str = secondArea_str;
    }
}
