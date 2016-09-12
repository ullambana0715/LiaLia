package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by cc on 16/7/25.
 */
public class GeneralVideos implements Serializable{

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */

    private static final long serialVersionUID = 1L;
    /**
     * videoId : 1
     * videoUrl : sample string 2
     * coverImgUrl : sample string 3
     */

    private int videoId; // V3.1 视频编号
    private String videoUrl; // V3.1 视频链接
    private String coverImgUrl;// V3.1 封面图片，原始图片大小同上传图片（如果要小图片，使用url参数 w=30 h=30)
    private int praiseCount;// 点赞数量
    private int praiseStatus;// 点赞状态（1:点赞过 2:未点赞过）

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

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(int praiseStatus) {
        this.praiseStatus = praiseStatus;
    }

    @Override
    public String toString() {
        return "GeneralVideos{" +
                "videoId=" + videoId +
                ", videoUrl='" + videoUrl + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", praiseCount=" + praiseCount +
                ", praiseStatus=" + praiseStatus +
                '}';
    }
}
