package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by jianghua on 2016/6/28.
 */

@Table(name = "MyActivitiesDto")
public class MyActivitiesDto extends EntityBase {
    @Column(column = "userId")
    private int userId;

    @Column(column = "resp")
    private String resp;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "MyActivitiesDto{" +
                "userId=" + userId +
                ", resp='" + resp + '\'' +
                '}';
    }
}
