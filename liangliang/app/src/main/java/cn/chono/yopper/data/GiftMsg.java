package cn.chono.yopper.data;

/**
 * Created by sunquan on 16/8/3.
 */
public class GiftMsg {

    private String type;

    private int charmValue;

    private String giftImg;


    //0不是咨询师，1是咨询师
    private int counsel;

    private String dateid;

    //0 -false  1-true
    private int mask;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCharmValue() {
        return charmValue;
    }

    public void setCharmValue(int charmValue) {
        this.charmValue = charmValue;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
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

    public int getCounsel() {
        return counsel;
    }

    public void setCounsel(int counsel) {
        this.counsel = counsel;
    }


    public GiftMsg() {

    }


    public GiftMsg(String type, int charmValue, String giftImg, int counsel, String dateid, int mask) {
        this.type = type;
        this.charmValue = charmValue;
        this.giftImg = giftImg;
        this.counsel = counsel;
        this.dateid = dateid;
        this.mask = mask;
    }
}
