package cn.chono.yopper.event;

/**
 * Created by sunquan on 16/6/29.
 */
public class NoSatisfyApple {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NoSatisfyApple(String message) {
        this.message = message;
    }

    public NoSatisfyApple() {
    }
}
