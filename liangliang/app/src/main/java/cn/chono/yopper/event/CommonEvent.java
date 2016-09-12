package cn.chono.yopper.event;

/**
 * Created by cc on 16/7/1.
 */
public class CommonEvent<T> {

    public CommonEvent(T event) {
        this.event = event;
    }

    public CommonEvent() {

    }

    public CommonEvent(int postion, T event) {
        this.postion = postion;
        this.event = event;
    }

    public CommonEvent(int postion) {
        this.postion = postion;
    }

    public int postion;

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public  T event;


    public T getEvent() {
        return event;
    }

    public void setEvent(T event) {
        this.event = event;
    }

}
