package cn.chono.yopper.entity.unlock;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/8/10.
 */
public class VerificationBean implements Serializable {
    private int ownerId;
    private String videoUrl;
    private String coverImgUrl;
    private int status;
    private boolean open;
    private int purpose;
    private boolean chatWithVideoUserOnly;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public boolean isChatWithVideoUserOnly() {
        return chatWithVideoUserOnly;
    }

    public void setChatWithVideoUserOnly(boolean chatWithVideoUserOnly) {
        this.chatWithVideoUserOnly = chatWithVideoUserOnly;
    }

    @Override
    public String toString() {
        return "VerificationBean{" +
                "ownerId=" + ownerId +
                ", videoUrl='" + videoUrl + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", status=" + status +
                ", open=" + open +
                ", purpose=" + purpose +
                ", chatWithVideoUserOnly=" + chatWithVideoUserOnly +
                '}';
    }
}
