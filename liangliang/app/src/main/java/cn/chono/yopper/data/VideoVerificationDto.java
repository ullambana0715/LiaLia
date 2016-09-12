package cn.chono.yopper.data;

/**
 * Created by sunquan on 16/5/19.
 */
public class VideoVerificationDto {

    //"#status": "视频认证状态 NotSubmited = 0,InAuditing = 1, Success = 2, Fail = 3",
    private int status;
    // "#purpose": "交友目的\r\n            MakeFriends = 1,交朋友;\r\n            FindLover = 2,找恋人;\r\n            ForMarriage = 3,找结婚对象",
    private int purpose;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }
    
}
