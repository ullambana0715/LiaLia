package cn.chono.yopper.Service.Http.Banners;

import java.io.Serializable;

/**
 * Created by cc on 16/2/23.
 */
public class BannersSubBanners implements Serializable {

    private String bannerId;

    private String name;

    private String iconUrl;

    private String description;

    private String iconUrl2;

    private boolean allowUserDefine;

    private String redirectUrl;

    private int seq;







    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl2() {
        return iconUrl2;
    }

    public void setIconUrl2(String iconUrl2) {
        this.iconUrl2 = iconUrl2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isAllowUserDefine() {
        return allowUserDefine;
    }

    public void setAllowUserDefine(boolean allowUserDefine) {
        this.allowUserDefine = allowUserDefine;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


}