package cn.chono.yopper.smack.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

//建议加上注解， 混淆后表名不受影响
//, execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON ChatDto(jid,message)"
@Table(name = "ChatDto")
public class ChatDto extends EntityBase {

    /**
     * 标记自已发送0
     */
    public final static int s_type = 0;
    /**
     * 标记对方的1
     */
    public final static int r_type = 1;

    /**
     * 标记未读状态０
     */
    public final static int no_read_status = 0;
    /**
     * 标读状态１
     */
    public final static int readed_status = 1;
    /**
     * 发送失败０
     */
    public final static int no_succeed = 0;
    /**
     * 发送成功１
     */
    public final static int succeed = 1;
    /**
     * 发送中
     */
    public final static int sending = 2;
    /**
     * 用户ＩＤ
     */
    @Column(column = "jid")
    // 建议加上注解， 混淆后列名不受影响
    private String jid;
    /**
     * 自己的ＩＤ
     */
    @Column(column = "mid")
    // 建议加上注解， 混淆后列名不受影响
    private String mid;
    /**
     * 消息内容
     */
    @Column(column = "message")
    // 建议加上注解， 混淆后列名不受影响
    private String message;
    /**
     * 时间
     */
    @Column(column = "date")
    // 建议加上注解， 混淆后列名不受影响
    private long date;
    /**
     * 0代表自己发送的，1代表自己对方的
     */
    @Column(column = "r_s_type")
    // 建议加上注解， 混淆后列名不受影响
    private int r_s_type;
    /**
     * 0未读，1,已读
     */
    @Column(column = "status")
    // 建议加上注解， 混淆后列名不受影响
    private int status;
    /**
     * 包ID
     */
    @Column(column = "msgId")
    // 建议加上注解， 混淆后列名不受影响
    private String msgId;
    /**
     * 发送状态0失败，１成功
     */
    @Column(column = "send_status")
    // 建议加上注解， 混淆后列名不受影响
    private int send_status;

    @Column(column = "TIMMessage")
    private String TIMMessage;

    /**
     * 邀约ID
     */
    @Column(column = "datingId")
    // 建议加上注解， 混淆后列名不受影响
    private String datingId;


    /**
     * 咨询消息类型
     */
    @Column(column = "counsel")
    // 建议加上注解， 混淆后列名不受影响
    private int counsel;


    public ChatDto(String mid, String jid, String message, long date, int r_s_type, int status, String msgId, int send_status, String datingId, int counsel,String TIMMessage) {
        super();
        this.jid = jid;
        this.mid = mid;
        this.message = message;
        this.date = date;
        this.r_s_type = r_s_type;
        this.status = status;
        this.msgId = msgId;
        this.send_status = send_status;
        this.datingId = datingId;
        this.counsel = counsel;
        this.TIMMessage=TIMMessage;
    }

    public ChatDto() {
        super();

    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }


    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getR_s_type() {
        return r_s_type;
    }

    public void setR_s_type(int r_s_type) {
        this.r_s_type = r_s_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getSend_status() {
        return send_status;
    }

    public void setSend_status(int send_status) {
        this.send_status = send_status;
    }

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    public String getMid() {
        return mid;
    }

    public String getTIMMessage() {
        return TIMMessage;
    }

    public void setTIMMessage(String TIMMessage) {
        this.TIMMessage = TIMMessage;
    }

    public int getCounsel() {
        return counsel;
    }

    public void setCounsel(int counsel) {
        this.counsel = counsel;
    }



}
