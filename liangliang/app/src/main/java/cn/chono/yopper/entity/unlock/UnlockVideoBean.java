package cn.chono.yopper.entity.unlock;

import java.io.Serializable;

/**
 * Created by jianghua on 2016/7/27.
 */
public class UnlockVideoBean implements Serializable {


    /**
     * result : 0
     * msg : sample string 1
     * videoId : 2
     * videoUrl : sample string 3
     * coverImgUrl : sample string 4
     */

    private int result;
    private String msg;
    private int videoId;
    private String videoUrl;
    private String coverImgUrl;

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

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
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

    @Override
    public String toString() {
        return "UnlockVideoBean{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", videoId=" + videoId +
                ", videoUrl='" + videoUrl + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                '}';
    }
}
