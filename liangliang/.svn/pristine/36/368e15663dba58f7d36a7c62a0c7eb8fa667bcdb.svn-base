package cn.chono.yopper.Service.DaillyTaskService;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import cn.chono.yopper.data.EntityBase;

/**
 * Created by zxb on 2015/11/25.
 */
@Table(name = "DaillyBean")
public class DaillyBean extends EntityBase {

    @Column(column = "userID")

    private int userID;

    @Column(column = "Time")
    private long Time;

    @Column(column = "tag")
    private String tag;

    @Column(column = "isState")
    private boolean isState = false;

    @Column(column = "data")
    private String data = "";

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isState() {
        return isState;
    }

    public void setState(boolean state) {
        isState = state;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
