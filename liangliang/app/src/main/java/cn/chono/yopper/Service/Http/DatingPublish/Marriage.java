package cn.chono.yopper.Service.Http.DatingPublish;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * 征婚
 * Created by cc on 16/3/31.
 */
public class Marriage implements Parcelable {

    private int parentsBeingAlive;// 父母健在情况（0：双亲健在 1：父亲健在 2：母亲健在）（必填）

    private boolean isOnlyChild;// 是否独生子女（必填）


    private String permanentFirstArea;// 户籍地址一级地区（上海、浙江）（必填）

    private String permanentSecondArea;// 户籍地址二级地区（静安、杭州）（必填）

    private String presentFirstArea;// 现居地一级地区（上海、浙江）（必填）

    private String presentSecondArea; // 现居地二级地区（静安、杭州）（必填）

    private int income;// 月收入（1：3000以下 2：3000以上 3：5000以上 4：10000以上 5：20000以上 6：50000以上）（必填）

    private int loveHistory;// 恋爱史（0：恋爱啥滋味？ 1：一次 2：两次 3：三次 4：多次）（必填）

    private boolean hasMarriageHistory;// 是否有婚史（必填）

    private int childrenCondition;// 子女情况（0：无 1：有 2：有/孩子跟我 3：有/孩子跟我父母 4：孩子跟对方）（必填）

    private int [] drinkConditions;// 饮酒情况（0：经常饮酒 1：偶尔小酌 2：不会喝酒 3：偶尔饮葡萄酒 4：会喝一些啤酒 5：会喝一点 6：喜欢白酒 7：喜欢洋酒 8：喜欢啤酒 9：喜欢自酿酒 10：心情不好时喝 11：聚会应酬时喝）（必填）

    private int healthCondition;//健康自评（0：身体极好 1：一切正常 2：有点亚健康 3：有时候有些小毛病 4：目前不太好）（必填）

    private int marriedHouseCondition;// 婚后住房（0：家乡有房 1：计划购买，双方共担 2：计划购买，我肚子承担 3：已购房产 4：父母住房 5：单位配房）（必填）


    private String photoUrl;// 照片Url（必填）

    private int wishMarriageTime;// 结婚时间（0：三个月内结婚 1：一年内结婚 2：相处好结婚）（必填）

    private String profession;// 职业（必填）

    private int education;// 学历（0：高中 1：中专 2：大专 3：本科 4：双学士 5：硕士 6：博士 7：博士后）（必填）

    private int height;// 身高（必填）

    private int weight;// 体重（必填）

    private String wish;// 寄语 （必填）

    public int getParentsBeingAlive() {
        return parentsBeingAlive;
    }

    public void setParentsBeingAlive(int parentsBeingAlive) {
        this.parentsBeingAlive = parentsBeingAlive;
    }

    public boolean isOnlyChild() {
        return isOnlyChild;
    }

    public void setOnlyChild(boolean onlyChild) {
        isOnlyChild = onlyChild;
    }

    public String getPermanentFirstArea() {
        return permanentFirstArea;
    }

    public void setPermanentFirstArea(String permanentFirstArea) {
        this.permanentFirstArea = permanentFirstArea;
    }

    public String getPermanentSecondArea() {
        return permanentSecondArea;
    }

    public void setPermanentSecondArea(String permanentSecondArea) {
        this.permanentSecondArea = permanentSecondArea;
    }

    public String getPresentFirstArea() {
        return presentFirstArea;
    }

    public void setPresentFirstArea(String presentFirstArea) {
        this.presentFirstArea = presentFirstArea;
    }

    public String getPresentSecondArea() {
        return presentSecondArea;
    }

    public void setPresentSecondArea(String presentSecondArea) {
        this.presentSecondArea = presentSecondArea;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getLoveHistory() {
        return loveHistory;
    }

    public void setLoveHistory(int loveHistory) {
        this.loveHistory = loveHistory;
    }

    public boolean isHasMarriageHistory() {
        return hasMarriageHistory;
    }

    public void setHasMarriageHistory(boolean hasMarriageHistory) {
        this.hasMarriageHistory = hasMarriageHistory;
    }

    public int getChildrenCondition() {
        return childrenCondition;
    }

    public void setChildrenCondition(int childrenCondition) {
        this.childrenCondition = childrenCondition;
    }

    public int[] getDrinkConditions() {
        return drinkConditions;
    }

    public void setDrinkConditions(int[] drinkConditions) {
        this.drinkConditions = drinkConditions;
    }

    public int getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(int healthCondition) {
        this.healthCondition = healthCondition;
    }

    public int getMarriedHouseCondition() {
        return marriedHouseCondition;
    }

    public void setMarriedHouseCondition(int marriedHouseCondition) {
        this.marriedHouseCondition = marriedHouseCondition;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getWishMarriageTime() {
        return wishMarriageTime;
    }

    public void setWishMarriageTime(int wishMarriageTime) {
        this.wishMarriageTime = wishMarriageTime;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    @Override
    public String toString() {
        return "Marriage{" +
                "parentsBeingAlive=" + parentsBeingAlive +
                ", isOnlyChild=" + isOnlyChild +
                ", permanentFirstArea='" + permanentFirstArea + '\'' +
                ", permanentSecondArea='" + permanentSecondArea + '\'' +
                ", presentFirstArea='" + presentFirstArea + '\'' +
                ", presentSecondArea='" + presentSecondArea + '\'' +
                ", income=" + income +
                ", loveHistory=" + loveHistory +
                ", hasMarriageHistory=" + hasMarriageHistory +
                ", childrenCondition=" + childrenCondition +
                ", drinkConditions=" + Arrays.toString(drinkConditions) +
                ", healthCondition=" + healthCondition +
                ", marriedHouseCondition=" + marriedHouseCondition +
                ", photoUrl='" + photoUrl + '\'' +
                ", wishMarriageTime=" + wishMarriageTime +
                ", profession='" + profession + '\'' +
                ", education=" + education +
                ", height=" + height +
                ", weight=" + weight +
                ", wish='" + wish + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.parentsBeingAlive);
        dest.writeByte(isOnlyChild ? (byte) 1 : (byte) 0);
        dest.writeString(this.permanentFirstArea);
        dest.writeString(this.permanentSecondArea);
        dest.writeString(this.presentFirstArea);
        dest.writeString(this.presentSecondArea);
        dest.writeInt(this.income);
        dest.writeInt(this.loveHistory);
        dest.writeByte(hasMarriageHistory ? (byte) 1 : (byte) 0);
        dest.writeInt(this.childrenCondition);
        dest.writeIntArray(this.drinkConditions);
        dest.writeInt(this.healthCondition);
        dest.writeInt(this.marriedHouseCondition);
        dest.writeString(this.photoUrl);
        dest.writeInt(this.wishMarriageTime);
        dest.writeString(this.profession);
        dest.writeInt(this.education);
        dest.writeInt(this.height);
        dest.writeInt(this.weight);
        dest.writeString(this.wish);
    }

    public Marriage() {
    }

    protected Marriage(Parcel in) {
        this.parentsBeingAlive = in.readInt();
        this.isOnlyChild = in.readByte() != 0;
        this.permanentFirstArea = in.readString();
        this.permanentSecondArea = in.readString();
        this.presentFirstArea = in.readString();
        this.presentSecondArea = in.readString();
        this.income = in.readInt();
        this.loveHistory = in.readInt();
        this.hasMarriageHistory = in.readByte() != 0;
        this.childrenCondition = in.readInt();
        this.drinkConditions = in.createIntArray();
        this.healthCondition = in.readInt();
        this.marriedHouseCondition = in.readInt();
        this.photoUrl = in.readString();
        this.wishMarriageTime = in.readInt();
        this.profession = in.readString();
        this.education = in.readInt();
        this.height = in.readInt();
        this.weight = in.readInt();
        this.wish = in.readString();
    }

    public static final Parcelable.Creator<Marriage> CREATOR = new Parcelable.Creator<Marriage>() {
        @Override
        public Marriage createFromParcel(Parcel source) {
            return new Marriage(source);
        }

        @Override
        public Marriage[] newArray(int size) {
            return new Marriage[size];
        }
    };
}
