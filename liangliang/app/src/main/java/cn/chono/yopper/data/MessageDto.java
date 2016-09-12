package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import cn.chono.yopper.smack.entity.EntityBase;

/**
 * 消息列表消息体对象
 *
 * @author SQ
 */
@Table(name = "MessageDto")
public class MessageDto extends EntityBase {

    /**
     * 用户-用户
     */
    public final static int resource_user = 0;
    /**
     * 用户-咨询师
     */
    public final static int resource_counsel = 1;

    /**
     * 用户-系统
     */
    public final static int resource_system = 2;


    //对话最后一条消息
    @Column(column = "lastmessage")
    private String lastmessage;


    @Column(column = "TIMMessage")
    private String TIMMessage;


    @Column(column = "lasttime")
    private long lasttime;

    @Column(column = "mid")
    private String mid;

    @Column(column = "jid")
    private String jid;

    //0是失败 1是成功 2发送中
    @Column(column = "send_state")
    private int send_state;


    //0为用户-用户，1是用户-咨询师，2为用户-系统
    @Column(column = "resource")
    private int resource;

    //未读数量
    @Column(column = "no_read_num")
    private int no_read_num;

    //发送还是接受的消息
    @Column(column = "s_r_type")
    private int s_r_type;


    //邀约id
    @Column(column = "datingId")
    private String datingId;


    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public long getLasttime() {
        return lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public int getSend_state() {
        return send_state;
    }

    public void setSend_state(int send_state) {
        this.send_state = send_state;
    }


    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getNo_read_num() {
        return no_read_num;
    }

    public void setNo_read_num(int no_read_num) {
        this.no_read_num = no_read_num;
    }


    public int getS_r_type() {
        return s_r_type;
    }

    public void setS_r_type(int s_r_type) {
        this.s_r_type = s_r_type;
    }


    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    public String getTIMMessage() {
        return TIMMessage;
    }

    public void setTIMMessage(String TIMMessage) {
        this.TIMMessage = TIMMessage;
    }

    public MessageDto(String lastmessage, long lasttime, String mid, String jid, int send_state, int resource, int no_read_num, int s_r_type, String datingId,String TIMMessage) {
        super();
        this.lastmessage = lastmessage;
        this.lasttime = lasttime;
        this.mid = mid;
        this.jid = jid;
        this.send_state = send_state;
        this.resource = resource;
        this.no_read_num = no_read_num;
        this.s_r_type = s_r_type;
        this.datingId = datingId;
        this.TIMMessage = TIMMessage;
    }

    public MessageDto() {
        super();

    }

}
