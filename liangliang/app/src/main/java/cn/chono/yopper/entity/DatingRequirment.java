package cn.chono.yopper.entity;

/**
 * Created by SQ on 2016/1/7.
 */
public class DatingRequirment {
    private String name;
    private String msg;
    private boolean ready;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}