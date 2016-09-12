package cn.chono.yopper.Service.Http.CounselorsProfile;

/**
 * Created by cc on 16/5/3.
 */
public class CounselorProfileEntity {

    public int userId;// 用户编号

    public int sex;// 性别

    public int  maxCountEveryTime;// 服务库存单位量

    public int totalAnswerCount;// 已解答个数

    public boolean isFullReservation;// 预约是否满

    public ServiceTypePriceEntity serviceTypePrice; // 服务类型及价格

    public String avatar; // 头像地址

    public String [] albumImages;// 相册地址列表

    public String nickName;// 昵称

    public String personalDesc;// 个人简介


    public String [] skillTags;// 擅长标签

    public String storeAddress;// 门店


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getMaxCountEveryTime() {
        return maxCountEveryTime;
    }

    public void setMaxCountEveryTime(int maxCountEveryTime) {
        this.maxCountEveryTime = maxCountEveryTime;
    }

    public boolean isFullReservation() {
        return isFullReservation;
    }

    public void setFullReservation(boolean fullReservation) {
        isFullReservation = fullReservation;
    }

    public int getTotalAnswerCount() {
        return totalAnswerCount;
    }

    public void setTotalAnswerCount(int totalAnswerCount) {
        this.totalAnswerCount = totalAnswerCount;
    }

    public ServiceTypePriceEntity getServiceTypePrice() {
        return serviceTypePrice;
    }

    public void setServiceTypePrice(ServiceTypePriceEntity serviceTypePrice) {
        this.serviceTypePrice = serviceTypePrice;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String[] getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(String[] albumImages) {
        this.albumImages = albumImages;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPersonalDesc() {
        return personalDesc;
    }

    public void setPersonalDesc(String personalDesc) {
        this.personalDesc = personalDesc;
    }

    public String[] getSkillTags() {
        return skillTags;
    }

    public void setSkillTags(String[] skillTags) {
        this.skillTags = skillTags;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
}
