package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by sunquan on 16/5/8.
 * 用户与用户基于邀约的数据表 存储：邀约的基本信息 聊天是否回复   邀约的处理状态(接受 感兴趣 拒绝)
 */
//建议加上注解， 混淆后表名不受影响
//, execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON ChatDto(jid,message)"
@Table(name = "UserToUserWithDatingTable")
public class UserToUserWithDatingTable extends EntityBase {

    //主动
    public static final int meActive = 1;
    //被动
    public static final int me_no_Active = 2;

    //未回复
    public static final int replyed = 2;
    //已回复
    public static final int no_reply = 1;


    //同意
    public static final int status_agree = 3;


    //拒绝
    public static final int status_deny = 4;

    //再考虑
    public static final int status_delay = 2;


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
     * 邀约名称
     */
    @Column(column = "datingTheme")
    // 建议加上注解， 混淆后列名不受影响
    private String datingTheme;


    /**
     * 主动方还是被动方
     * 1代表主动 2代表被动
     */
    @Column(column = "meIsActive")
    // 建议加上注解， 混淆后列名不受影响
    private int meIsActive;

    /**
     * 是否回复
     * 1代表未回复 2代表已回复
     */
    @Column(column = "isReply")
    // 建议加上注解， 混淆后列名不受影响
    private int isReply;


    /**
     * 邀约所属用户ID
     */
    @Column(column = "publishDate_userId")
    // 建议加上注解， 混淆后列名不受影响
    private String publishDate_userId;


    /**
     * 邀约处理状态
     * 0无处理 1同意 2拒绝 3再考虑
     */
    @Column(column = "datingDealStatus")
    // 建议加上注解， 混淆后列名不受影响
    private int datingDealStatus;



    public UserToUserWithDatingTable(String targetId, String datingId, String userId, String datingTheme, int meIsActive, int isReply, int datingDealStatus, String publishDate_userId) {
        super();
        this.targetId = targetId;
        this.datingId = datingId;
        this.userId = userId;
        this.datingTheme = datingTheme;
        this.meIsActive = meIsActive;
        this.isReply = isReply;
        this.datingDealStatus = datingDealStatus;
        this.publishDate_userId = publishDate_userId;
    }

    public UserToUserWithDatingTable() {
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

    public String getDatingTheme() {
        return datingTheme;
    }

    public void setDatingTheme(String datingTheme) {
        this.datingTheme = datingTheme;
    }

    public int getMeIsActive() {
        return meIsActive;
    }

    public void setMeIsActive(int meIsActive) {
        this.meIsActive = meIsActive;
    }

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public int getDatingDealStatus() {
        return datingDealStatus;
    }

    public void setDatingDealStatus(int datingDealStatus) {
        this.datingDealStatus = datingDealStatus;
    }

    public String getPublishDate_userId() {
        return publishDate_userId;
    }

    public void setPublishDate_userId(String publishDate_userId) {
        this.publishDate_userId = publishDate_userId;
    }


}
