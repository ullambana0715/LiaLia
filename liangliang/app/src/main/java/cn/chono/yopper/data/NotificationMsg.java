package cn.chono.yopper.data;

import java.util.Map;

/**
 * Created by SQ on 2015/12/9.
 */
public class NotificationMsg {

    public String  from;

    public String category;

    //消息类型
    public String   type;

    /// 类型
    public int notifytype;

    /// 通知文本
    public String text;

    /// 通知内部显示的头像
    public String avatar;

    /// 通知标题
    public String title;

    /// 通知内容
    public String content;

    /// 通知角标
    public String icon;

    /// 显示小红点数字提示，-1不显示，0显示小红点，>0显示对应数字
    public int badge;

    /// 小于等于0不提示声音，大于0提示对应编号的声音
    public int sound;

    /// 额外信息（点击后需要）
    public Map extra;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNotifytype() {
        return notifytype;
    }

    public void setNotifytype(int notifytype) {
        this.notifytype = notifytype;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public Map getExtra() {
        return extra;
    }

    public void setExtra(Map extra) {
        this.extra = extra;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
