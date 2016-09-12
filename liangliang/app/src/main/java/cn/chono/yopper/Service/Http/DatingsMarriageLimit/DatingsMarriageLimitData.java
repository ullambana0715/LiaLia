package cn.chono.yopper.Service.Http.DatingsMarriageLimit;

/**
 * Created by cc on 16/4/6.
 */
public class DatingsMarriageLimitData {

    private boolean isPass;

    private String msg;

    private String datingId;


    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDatingId() {
        return datingId;
    }

    public void setDatingId(String datingId) {
        this.datingId = datingId;
    }

    @Override
    public String toString() {
        return "DatingsMarriageLimitData{" +
                "isPass=" + isPass +
                ", msg='" + msg + '\'' +
                ", datingId='" + datingId + '\'' +
                '}';
    }
}
