package cn.chono.yopper.data;

import java.io.Serializable;

public class Profile implements Serializable{
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */

	private static final long serialVersionUID = 1L;

	private Integer relationship;
	private Integer age;
	private String birthday;
	private boolean birthdayPrivacy;

	private String career;
	private Integer height;
	private Integer weight;

	private String hometown;
	private String likes;

	private int attraction;
	private int beautyScore;

	private Integer incomeLevel;

	private String tags;

	private int completion;

	private String mobile;
	private String hashedPassword;

	private String headImgInternal;

	//头像状态 
	private int  status;

	private int id;
	private String name;
	private int horoscope;
	private String headImg;
	private int sex;
	private String regTime;
	private String dislikes;

	private int cityCode;

	private int  level;

	private String uid;



	@Override
	public String toString() {
		return "Profile [relationship=" + relationship + ", age=" + age
				+ ", birthday=" + birthday + ", birthdayPrivacy="
				+ birthdayPrivacy + ", career=" + career + ", height=" + height
				+ ", weight=" + weight + ", hometown=" + hometown + ", likes="
				+ likes + ", attraction=" + attraction + ", beautyScore="
				+ beautyScore + ", incomeLevel=" + incomeLevel + ", tags="
				+ tags + ", completion=" + completion + ", mobile=" + mobile
				+ ", hashedPassword=" + hashedPassword + ", status=" + status
				+ ", id=" + id + ", name=" + name + ", horoscope=" + horoscope
				+ ", headImg=" + headImg + ", sex=" + sex + ", regTime="
				+ regTime + ", dislikes=" + dislikes + "]";
	}


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}


	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCityCode() {
		return cityCode;
	}

	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getRelationship() {
		return relationship;
	}
	public void setRelationship(Integer relationship) {
		this.relationship = relationship;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public boolean isBirthdayPrivacy() {
		return birthdayPrivacy;
	}
	public void setBirthdayPrivacy(boolean birthdayPrivacy) {
		this.birthdayPrivacy = birthdayPrivacy;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	public int getAttraction() {
		return attraction;
	}
	public void setAttraction(int attraction) {
		this.attraction = attraction;
	}
	public int getBeautyScore() {
		return beautyScore;
	}
	public void setBeautyScore(int beautyScore) {
		this.beautyScore = beautyScore;
	}
	public Integer getIncomeLevel() {
		return incomeLevel;
	}
	public void setIncomeLevel(Integer incomeLevel) {
		this.incomeLevel = incomeLevel;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getCompletion() {
		return completion;
	}
	public void setCompletion(int completion) {
		this.completion = completion;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getDislikes() {
		return dislikes;
	}
	public void setDislikes(String dislikes) {
		this.dislikes = dislikes;
	}

	public String getHeadImgInternal() {
		return headImgInternal;
	}

	public void setHeadImgInternal(String headImgInternal) {
		this.headImgInternal = headImgInternal;
	}
}
