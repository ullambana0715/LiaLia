package cn.chono.yopper.data;

/**
 * Created by sunquan on 16/5/9.
 */
public class DatingHandleStatusMsg {


    private String type;

    private String dateid;

    private String text;

    //0无处理 1同意 2拒绝 3考虑
    private int datingHandleStatus;

    private int mask;

    private String datingTheme;

    private String publishDate_userId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateid() {
        return dateid;
    }

    public void setDateid(String dateid) {
        this.dateid = dateid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDatingHandleStatus() {
        return datingHandleStatus;
    }

    public void setDatingHandleStatus(int datingHandleStatus) {
        this.datingHandleStatus = datingHandleStatus;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public String getDatingTheme() {
        return datingTheme;
    }

    public void setDatingTheme(String datingTheme) {
        this.datingTheme = datingTheme;
    }

    public String getPublishDate_userId() {
        return publishDate_userId;
    }

    public void setPublishDate_userId(String publishDate_userId) {
        this.publishDate_userId = publishDate_userId;
    }
}
