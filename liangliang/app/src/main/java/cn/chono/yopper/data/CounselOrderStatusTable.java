package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 用于数据库缓存咨询订单信息
 *
 * @author SQ
 */

@Table(name = "CounselOrderStatusTable")
public class CounselOrderStatusTable extends EntityBase {

    public static final int no_start = 1;

    public static final int counseling = 2;

    public static final int request_end = 3;

    public static final int counselEnd = 4;


    @Column(column = "mid")
    private String mid;


    @Column(column = "jid")
    private String jid;


    @Column(column = "orderId")
    private String orderId;

    @Column(column = "counselOrderStatus")
    private int counselOrderStatus;


    @Column(column = "hint")
    private String hint;


    @Column(column = "counselorType")
    private int counselorType;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getCounselOrderStatus() {
        return counselOrderStatus;
    }

    public void setCounselOrderStatus(int counselOrderStatus) {
        this.counselOrderStatus = counselOrderStatus;
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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }


    public int getCounselorType() {
        return counselorType;
    }

    public void setCounselorType(int counselorType) {
        this.counselorType = counselorType;
    }
}
