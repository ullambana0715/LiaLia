package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by cc on 16/7/25.
 */
public class Appointments implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */

    private static final long serialVersionUID = 1L;


    /**
     * id : sample string 1
     * title : sample string 2
     * activityType : 3
     * content : sample string 4
     * endTime : 2016-07-25T09:49:04.50712+08:00
     */

    private String id;
    private String title;
    private int activityType;
    private String content;
    private String endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
