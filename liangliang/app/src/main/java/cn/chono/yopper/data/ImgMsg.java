package cn.chono.yopper.data;

import com.tencent.TIMImageElem;

public class ImgMsg {

    private TIMImageElem elem;
    private String type;

    private double w;
    private double h;

    private String id;

    private int counsel;

    private String dateid;

    private int mask;


    private String filePath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
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

    public TIMImageElem getElem() {
        return elem;
    }

    public void setElem(TIMImageElem elem) {
        this.elem = elem;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
