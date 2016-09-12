package cn.chono.yopper.entity.likeBean;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/1.
 */
public class CheckKeyBean implements Serializable {
    /**
     * result : 0
     * msg : sample string 1
     * consumerKeyCount : 2
     * userKeyCount : 3
     */

    private int result;
    private String msg;
    private int consumerKeyCount;
    private int userKeyCount;

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

    public int getConsumerKeyCount() {
        return consumerKeyCount;
    }

    public void setConsumerKeyCount(int consumerKeyCount) {
        this.consumerKeyCount = consumerKeyCount;
    }



    public int getUserKeyCount() {
        return userKeyCount;
    }

    public void setUserKeyCount(int userKeyCount) {
        this.userKeyCount = userKeyCount;
    }

    @Override
    public String toString() {
        return "CheckKeyBean{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", consumerKeyCount=" + consumerKeyCount +
                ", userKeyCount=" + userKeyCount +
                '}';
    }
}
