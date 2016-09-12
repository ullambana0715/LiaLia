package cn.chono.yopper.Service.Http.Banners;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cc on 16/2/22.
 */
public class BannersData implements Serializable {

    private String bannerId;

    private String name;

    private String iconUrl;

    private String description;

    private boolean allowUserDefine;

    private String redirectUrl;

    private int seq;

    private List<BannersSubBanners> subBanners;


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

    public List<BannersSubBanners> getSubBanners() {
        return subBanners;
    }

    public void setSubBanners(List<BannersSubBanners> subBanners) {
        this.subBanners = subBanners;
    }


    public boolean isAllowUserDefine() {
        return allowUserDefine;
    }

    public void setAllowUserDefine(boolean allowUserDefine) {
        this.allowUserDefine = allowUserDefine;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
