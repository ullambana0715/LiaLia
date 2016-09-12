package cn.chono.yopper.event;

/**
 * Created by sunquan on 16/5/31.
 */
public class BubblingListEvent {

    //定义事件类型
    private int eventType;

    public BubblingListEvent() {
    }

    public BubblingListEvent(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
