package cn.chono.yopper.entity.unlock;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/7/26.
 */
public class PrivateAlbumBean implements Serializable {
    /**
     * result : 0
     * msg : sample string 1
     */

    private int result;
    private String msg;

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

    @Override
    public String toString() {
        return "PrivateAlbumBean{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
