package cn.chono.yopper.data;

import com.tencent.TIMSoundElem;

/**
 * Created by sunquan on 16/5/9.
 */
public class AudioMsg {

    private TIMSoundElem elem;

    //音频的类型
    private String type;

    private int duration;

    private int counsel;

    private String dateid;

    private int mask;

    private String filepath;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCounsel() {
        return counsel;
    }

    public void setCounsel(int counsel) {
        this.counsel = counsel;
    }


    public String getDateid() {
        return dateid;
    }

    public void setDateid(String dateid) {
        this.dateid = dateid;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public TIMSoundElem getElem() {
        return elem;
    }

    public void setElem(TIMSoundElem elem) {
        this.elem = elem;
    }


    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

}
