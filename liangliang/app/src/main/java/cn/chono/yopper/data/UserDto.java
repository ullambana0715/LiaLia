package cn.chono.yopper.data;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.entity.PrivacyAlbum;

public class UserDto implements Serializable {


    private boolean isFirstCall;

    private boolean isHot;

    // V2.5.4 喜欢
    private boolean isLike;

    // V3.1 喜欢：0表示不喜欢，1表示我喜欢，2表示喜欢我，3表示相互喜欢
    private int userLikeState;

    // V2.5.4 是否活动达人
    private boolean isActivityExpert;

    // V2.5.4 用户身份 ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    private int currentUserPosition;

    // V2.5.4 历史vip身份  1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP
    private int lastUserVipPosition;

    private Profile profile;

    private Wish latestWish;

    private int horoscopeScore;

    private Stats stats;


    private String[] album;


    private String albumMask; //"#albumMask": "用户相册权限字符串\r\n            example: 1010101001 \r\n            从左边算，代表第一张，第二张，第四张，第六张，第九张照片有权限，其他没有权限",

    private int albumMax; // "#albumMax": "相册最大数量",

    // V3.1 私密相册
    private List<PrivacyAlbum> privacyAlbum;

    private int privacyAlbumMax;// 私密相册最大数量

    // V3.1 是否解锁 私密相册
    private boolean isUnlockPrivacyAlbum;

    private Bubble bubble;





    private VideoVerificationDto videoVerification;

    private int generalVideoMax;// 形象视频最大数量

    // V3.1 形象视频
    private List<GeneralVideos> generalVideos;

    private double distance;

    private String lastActiveTime;


    private UserFaceRatingDto faceRating;

    // 约会列表, v2.4
    private List<Appointments> appointments;



    // 资料是否完整
    private boolean isProfileComplete;

    private int todayLucky;//今日运势 0-100 满分五颗星星

    private int horoscopeMatch;//星座匹配 0-100 满分五颗星星

    // 她的壕友列表
    private List<HoYo> transferedUsers;

    // 照片信息（照片点赞功能用）
    private List<UserPhoto> photos;

    private List<AppointDetailDto> datingList;// 用户邀约信息


    // 我的礼物合计
    private List<MyGiftSum> myGiftSum;




    public List<AppointDetailDto> getDatingList() {
        return datingList;
    }

    public void setDatingList(List<AppointDetailDto> datingList) {
        this.datingList = datingList;
    }

    public List<UserPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<UserPhoto> photos) {
        this.photos = photos;
    }

    public List<HoYo> getTransferedUsers() {
        return transferedUsers;
    }

    public void setTransferedUsers(List<HoYo> transferedUsers) {
        this.transferedUsers = transferedUsers;
    }

    public boolean isFirstCall() {
        return isFirstCall;
    }

    public void setFirstCall(boolean firstCall) {
        isFirstCall = firstCall;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isActivityExpert() {
        return isActivityExpert;
    }

    public void setActivityExpert(boolean activityExpert) {
        isActivityExpert = activityExpert;
    }

    public int getCurrentUserPosition() {
        return currentUserPosition;
    }

    public void setCurrentUserPosition(int currentUserPosition) {
        this.currentUserPosition = currentUserPosition;
    }

    public int getLastUserVipPosition() {
        return lastUserVipPosition;
    }

    public void setLastUserVipPosition(int lastUserVipPosition) {
        this.lastUserVipPosition = lastUserVipPosition;
    }

    public boolean isProfileComplete() {
        return isProfileComplete;
    }

    public void setProfileComplete(boolean profileComplete) {
        isProfileComplete = profileComplete;
    }

    public String getAlbumMask() {
        return albumMask;
    }

    public void setAlbumMask(String albumMask) {
        this.albumMask = albumMask;
    }

    public int getAlbumMax() {
        return albumMax;
    }

    public void setAlbumMax(int albumMax) {
        this.albumMax = albumMax;
    }

    public VideoVerificationDto getVideoVerification() {
        return videoVerification;
    }

    public void setVideoVerification(VideoVerificationDto videoVerification) {
        this.videoVerification = videoVerification;
    }

    public double getDistance() {
        return distance;
    }

    public void setBubble(Bubble bubble) {
        this.bubble = bubble;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Wish getLatestWish() {
        return latestWish;
    }

    public void setLatestWish(Wish latestWish) {
        this.latestWish = latestWish;
    }

    public int getHoroscopeScore() {
        return horoscopeScore;
    }

    public void setHoroscopeScore(int horoscopeScore) {
        this.horoscopeScore = horoscopeScore;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }


    public UserFaceRatingDto getFaceRating() {
        return faceRating;
    }

    public void setFaceRating(UserFaceRatingDto faceRating) {
        this.faceRating = faceRating;
    }

    public int getTodayLucky() {
        return todayLucky;
    }

    public void setTodayLucky(int todayLucky) {
        this.todayLucky = todayLucky;
    }

    public int getHoroscopeMatch() {
        return horoscopeMatch;
    }

    public void setHoroscopeMatch(int horoscopeMatch) {
        this.horoscopeMatch = horoscopeMatch;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public Bubble getBubble() {
        return bubble;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public int getUserLikeState() {
        return userLikeState;
    }

    public void setUserLikeState(int userLikeState) {
        this.userLikeState = userLikeState;
    }



    public boolean isUnlockPrivacyAlbum() {
        return isUnlockPrivacyAlbum;
    }

    public void setUnlockPrivacyAlbum(boolean unlockPrivacyAlbum) {
        isUnlockPrivacyAlbum = unlockPrivacyAlbum;
    }

    public List<GeneralVideos> getGeneralVideos() {
        return generalVideos;
    }

    public void setGeneralVideos(List<GeneralVideos> generalVideos) {
        this.generalVideos = generalVideos;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }

    public List<MyGiftSum> getMyGiftSum() {
        return myGiftSum;
    }

    public void setMyGiftSum(List<MyGiftSum> myGiftSum) {
        this.myGiftSum = myGiftSum;
    }

    public int getPrivacyAlbumMax() {
        return privacyAlbumMax;
    }

    public void setPrivacyAlbumMax(int privacyAlbumMax) {
        this.privacyAlbumMax = privacyAlbumMax;
    }

    public int getGeneralVideoMax() {
        return generalVideoMax;
    }

    public void setGeneralVideoMax(int generalVideoMax) {
        this.generalVideoMax = generalVideoMax;
    }

    public String[] getAlbum() {
        return album;
    }

    public void setAlbum(String[] album) {
        this.album = album;
    }

    public List<PrivacyAlbum> getPrivacyAlbum() {
        return privacyAlbum;
    }

    public void setPrivacyAlbum(List<PrivacyAlbum> privacyAlbum) {
        this.privacyAlbum = privacyAlbum;
    }
}
