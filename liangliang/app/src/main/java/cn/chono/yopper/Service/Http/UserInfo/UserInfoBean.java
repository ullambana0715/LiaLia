package cn.chono.yopper.Service.Http.UserInfo;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class UserInfoBean extends ParameterBean{

    private int userId;

    private boolean NewAlbum;

    private boolean Wish;

    private boolean Bubble;
    private boolean Dating;
    private boolean Verification;

    private boolean Friends;

    private double Lng=-1;
    private double Lat=-1;

    private boolean faceRating;

    public boolean isFaceRating() {
        return faceRating;
    }

    public void setFaceRating(boolean faceRating) {
        this.faceRating = faceRating;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isWish() {
        return Wish;
    }

    public void setWish(boolean wish) {
        Wish = wish;
    }

    public boolean isBubble() {
        return Bubble;
    }

    public void setBubble(boolean bubble) {
        Bubble = bubble;
    }


    public boolean isVerification() {
        return Verification;
    }

    public void setVerification(boolean verification) {
        Verification = verification;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }


    public boolean isNewAlbum() {
        return NewAlbum;
    }

    public void setNewAlbum(boolean newAlbum) {
        NewAlbum = newAlbum;
    }

    public boolean isDating() {
        return Dating;
    }

    public void setDating(boolean dating) {
        Dating = dating;
    }

    public boolean isFriends() {
        return Friends;
    }

    public void setFriends(boolean friends) {
        Friends = friends;
    }
}
