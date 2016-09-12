package cn.chono.yopper.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by SQ on 16/7/12.
 */
public class ApiResp<T> implements Serializable {


    public String status;
    public String msg;
    public String errCode;

    private T resp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public T getResp() {
        return resp;
    }

    public void setResp(T resp) {
        this.resp = resp;
    }


    public boolean isSuccess() {
        if (TextUtils.equals("200", this.status)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ApiResp{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", errCode='" + errCode + '\'' +
                ", resp=" + resp +
                '}';
    }

}
