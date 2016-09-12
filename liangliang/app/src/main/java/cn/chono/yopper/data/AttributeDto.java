package cn.chono.yopper.data;

/**
 * Created by sunquan on 16/7/13.
 */
public class AttributeDto {

    private String type;

    //0不是咨询师，1是咨询师
    private int counsel;

    private String dateid;

    //0 -false  1-true
    private int mask;


    private String lockText;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


    public String getLockText() {
        return lockText;
    }

    public void setLockText(String lockText) {
        this.lockText = lockText;
    }


    public AttributeDto(String type, int counsel, String dateid, int mask) {
        this.type = type;
        this.counsel = counsel;
        this.dateid = dateid;
        this.mask = mask;
    }


    public AttributeDto(String type, int counsel, String dateid, int mask, String lockText) {
        this.type = type;
        this.counsel = counsel;
        this.dateid = dateid;
        this.mask = mask;
        this.lockText = lockText;
    }


    public AttributeDto() {
    }
}
