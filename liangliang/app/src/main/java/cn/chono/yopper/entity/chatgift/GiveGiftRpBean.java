package cn.chono.yopper.entity.chatgift;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/2.
 */
public class GiveGiftRpBean implements Serializable {

    /**
     * result : 0
     * msg : sample string 1
     */

    private int result;
    private String msg;

    // 打招呼语（带礼物打招呼时，有值
    private String toSendWords;

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


    public String getToSendWords() {
        return toSendWords;
    }

    public void setToSendWords(String toSendWords) {
        this.toSendWords = toSendWords;
    }


    @Override
    public String toString() {
        return "GiveGiftRpBean{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", toSendWords='" + toSendWords + '\'' +
                '}';
    }
}
