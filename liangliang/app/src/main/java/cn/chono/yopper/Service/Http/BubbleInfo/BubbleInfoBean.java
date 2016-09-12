package cn.chono.yopper.Service.Http.BubbleInfo;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/21.
 */
public class BubbleInfoBean extends ParameterBean {
    private String BubbleId;

    private double Lat;

    private double Lng;


    public String getBubbleId() {
        return BubbleId;
    }

    public void setBubbleId(String bubbleId) {
        BubbleId = bubbleId;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }
}
