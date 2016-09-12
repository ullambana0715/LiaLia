package cn.chono.yopper.entity.chatgift;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

import cn.chono.yopper.data.EntityBase;

/**
 * Created by jianghua on 2016/8/1.
 */
@Table(name = "GiftBeanDto")
public class GiftBeanDto extends EntityBase implements Serializable{

    @Column(column = "resp")
    private String resp;

    @Column(column = "userId")
    @NoAutoIncrement()
    private int userId;

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
