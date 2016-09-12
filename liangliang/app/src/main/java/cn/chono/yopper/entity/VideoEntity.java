package cn.chono.yopper.entity;

import java.io.Serializable;

import cn.chono.yopper.entity.unlock.VerificationBean;

/**
 * Created by cc on 16/7/29.
 */
public class VideoEntity implements Serializable{


    /**
     * viewStatus : 0
     * verification : {"ownerId":1,"videoUrl":"sample string 2","coverImgUrl":"sample string 3","status":0,"open":true,"purpose":1,"chatWithVideoUserOnly":true}
     */

    private int viewStatus;
    /**
     * ownerId : 1
     * videoUrl : sample string 2
     * coverImgUrl : sample string 3
     * status : 0
     * open : true
     * purpose : 1
     * chatWithVideoUserOnly : true
     */

    private VerificationBean verification;

    public int getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(int viewStatus) {
        this.viewStatus = viewStatus;
    }

    public VerificationBean getVerification() {
        return verification;
    }

    public void setVerification(VerificationBean verification) {
        this.verification = verification;
    }

}
