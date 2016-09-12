package cn.chono.yopper.data;

import java.util.Map;

/**
 * Created by sunquan on 16/5/11.
 */
public class HintMsg {

    public String type;

    public String text;

    //1是邀约删除
    public int action;

    public Map extra;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Map getExtra() {
        return extra;
    }

    public void setExtra(Map extra) {
        this.extra = extra;
    }
}
