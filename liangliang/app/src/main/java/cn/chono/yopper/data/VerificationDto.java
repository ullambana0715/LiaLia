package cn.chono.yopper.data;

/**
 * Created by SQ on 2015/10/19.
 */
public class VerificationDto {
    //视频拥有者Id
    private int    ownerId;
    //视频链接
    private String videoUrl;
    //封面图片，原始图片大小同上传图片（如果要小图片，使用url参数 w=30 h=30)
    private String coverImgUrl;

    //视频认证状态 0 1 2 3
    private int  status;
    //是否公开
    private boolean open;
    //交友目的     MakeFriends = 1,交朋友;          FindLover = 2,找恋人;        ForMarriage = 3,找结婚对象
    private int  purpose;
    //拒绝非视频用户认证消息
    private boolean chatWithVideoUserOnly;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }


    public boolean isChatWithVideoUserOnly() {
        return chatWithVideoUserOnly;
    }

    public void setChatWithVideoUserOnly(boolean chatWithVideoUserOnly) {
        this.chatWithVideoUserOnly = chatWithVideoUserOnly;
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
}
