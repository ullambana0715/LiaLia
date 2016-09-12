package cn.chono.yopper.data;


public class LookVideoDto {

	
	private boolean isVideoVerified;

	private boolean isNew;
	
	private String lastVisitTime;
	
	private User user;
	
	
	public boolean isVideoVerified() {
		return isVideoVerified;
	}


	public void setVideoVerified(boolean isVideoVerified) {
		this.isVideoVerified = isVideoVerified;
	}


	public boolean isNew() {
		return isNew;
	}

 
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}


	public String getLastVisitTime() {
		return lastVisitTime;
	}


	public void setLastVisitTime(String lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public class User{

		private int id;

		private String mobile;

		private String hashedPassword;

		private String name;

		private int horoscope;

		private String headImg;

		private int sex;

		private int status;

		private String regTime;
		
		private int  cityCode;
		
		public int getCityCode() {
			return cityCode;
		}

		public void setCityCode(int cityCode) {
			this.cityCode = cityCode;
		}

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

		public String getRegTime() {
			return regTime;
		}

		public void setRegTime(String regTime) {
			this.regTime = regTime;
		}
		
	}
	

	
	
}
