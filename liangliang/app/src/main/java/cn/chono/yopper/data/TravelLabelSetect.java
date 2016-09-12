package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by cc on 16/3/21.
 */
public class TravelLabelSetect implements Serializable {

    private String content;

    private boolean isSelect;



    public TravelLabelSetect(String content,boolean isSelect){
        this.content=content;
        this.isSelect=isSelect;

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


}
