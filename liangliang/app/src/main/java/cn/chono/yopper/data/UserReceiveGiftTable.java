package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by sunquan on 16/5/8.
 * 用户与用户基于邀约是否接收礼物的数据表
 */
//建议加上注解， 混淆后表名不受影响
//, execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON ChatDto(jid,message)"
@Table(name = "UserReceiveGiftTable")
public class UserReceiveGiftTable extends EntityBase {

    //主动
    public static final int Received = 1;
    //被动
    public static final int no_Receive = 2;

    /**
     * 用户ＩＤ
     */
    @Column(column = "targetId")
    // 建议加上注解， 混淆后列名不受影响
    private String targetId;

    /**
     * 自己的ＩＤ
     */
    @Column(column = "userId")
    // 建议加上注解， 混淆后列名不受影响
    private String userId;

    /**
     * 邀约id
     */
    @Column(column = "datingId")
    // 建议加上注解， 混淆后列名不受影响
    private String datingId;


    /**
     * 礼物接收状态
     * 1接收 2未接收
     */
    @Column(column = "isGiftReceive")
    // 建议加上注解， 混淆后列名不受影响
    private int isGiftReceive;


    public UserReceiveGiftTable(String targetId, String datingId, String userId, int isGiftReceive) {
        super();
        this.targetId = targetId;
        this.datingId = datingId;
        this.userId = userId;
        this.isGiftReceive=isGiftReceive;
    }

    public UserReceiveGiftTable() {

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

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }


    public int getIsGiftReceive() {
        return isGiftReceive;
    }

    public void setIsGiftReceive(int isGiftReceive) {
        this.isGiftReceive = isGiftReceive;
    }
}
