package cn.chono.yopper.data;

import java.io.Serializable;

/**
 * Created by SQ on 2015/12/28.
 */
public class User implements Serializable{
    private static final transient  long serialVersionUID = 1L;
    private int id;
    private String uid;
    private String mobile;
    private String hashedPassword;
    private String name;
    private int horoscope;
    private String headImg;
    private String headImgInternal;
    private int sex;
    private int status;
    private int level;
    private String regTime;
    private int cityCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(int horoscope) {
        this.horoscope = horoscope;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHeadImgInternal() {
        return headImgInternal;
    }

    public void setHeadImgInternal(String headImgInternal) {
        this.headImgInternal = headImgInternal;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
