package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by sunquan on 16/5/8.
 * 用户与用户是否解锁的数据库表
 */
//建议加上注解， 混淆后表名不受影响
//, execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON ChatDto(jid,message)"
@Table(name = "KeyTable")
public class KeyTable extends EntityBase {


    public  static int no_broken=0;

    public  static int had_broken=1;

    /**
     * 用户ＩＤ
     * 129233
     */
    @Column(column = "targetId")
    // 建议加上注解， 混淆后列名不受影响
    private String targetId;

    /**
     * 自己的ＩＤ
     * 129233
     */
    @Column(column = "userId")
    // 建议加上注解， 混淆后列名不受影响
    private String userId;

    /**
     * 0代表未解锁，1代表已解锁
     *
     */
    @Column(column = "isBrokenKey")
    // 建议加上注解， 混淆后列名不受影响
    private int isBrokenKey;


    public KeyTable(String targetId, String userId, int isBrokenKey) {
        super();
        this.targetId = targetId;
        this.userId = userId;
        this.isBrokenKey = isBrokenKey;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIsBrokenKey() {
        return isBrokenKey;
    }

    public void setIsBrokenKey(int isBrokenKey) {
        this.isBrokenKey = isBrokenKey;
    }


    public KeyTable() {
    }

    @Override
    public String toString() {
        return "KeyTable{" +
                "targetId='" + targetId + '\'' +
                ", userId='" + userId + '\'' +
                ", isBrokenKey=" + isBrokenKey +
                '}';
    }
}
