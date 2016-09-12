package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class ReceiveGiftResultEntity implements Serializable {

    /**
     * result : 0
     * msg : sample string 1
     */
    // 签收结果（0：成功） 1：存在拉黑关系
    private int result;

    // 失败信息
    private String msg;

    // 魅力值总计
    private int totalCharm;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotalCharm() {
        return totalCharm;
    }

    public void setTotalCharm(int totalCharm) {
        this.totalCharm = totalCharm;
    }
}
