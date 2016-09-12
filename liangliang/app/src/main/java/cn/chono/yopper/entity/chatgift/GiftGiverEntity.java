package cn.chono.yopper.entity.chatgift;

/**
 * Created by sunquan on 16/8/5.
 */
public class GiftGiverEntity {

    // 用户Id
    private int userId;

    // 昵称
    private String name;

    // 对外头像
    private String headImg;

    // 性别
    private int sex;

    // 年龄
    private int age;

    // 返回true表示年龄保密, false表示不保密
    private boolean birthdayPrivacy;

    // 星座
    private int horoscope;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isBirthdayPrivacy() {
        return birthdayPrivacy;
    }

    public void setBirthdayPrivacy(boolean birthdayPrivacy) {
        this.birthdayPrivacy = birthdayPrivacy;
    }

    public int getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(int horoscope) {
        this.horoscope = horoscope;
    }
}
