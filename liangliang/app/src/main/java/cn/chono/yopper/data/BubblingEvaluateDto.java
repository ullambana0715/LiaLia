package cn.chono.yopper.data;

import java.io.Serializable;
import java.util.List;

public class BubblingEvaluateDto implements Serializable {

	private int rows;
	private int start;
	private int total;
	private List<BubblingEvaluate> list;
	
	
	

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<BubblingEvaluate> getList() {
		return list;
	}

	public void setList(List<BubblingEvaluate> list) {
		this.list = list;
	}

	public class BubblingEvaluate implements Serializable {
		private String id;
		private String comment;

		private ToUser toUser;
		private User user;
		private String createTime;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public ToUser getToUser() {
			return toUser;
		}

		public void setToUser(ToUser toUser) {
			this.toUser = toUser;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

	}

	public class User implements Serializable {

		private int id;
		private String name;
		private String headImg;
		private int sex;
		private int horoscope;

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

		public int getHoroscope() {
			return horoscope;
		}

		public void setHoroscope(int horoscope) {
			this.horoscope = horoscope;
		}

	}

	public class ToUser implements Serializable {

		private int id;
		private String name;
		private String headImg;
		private int sex;
		private int horoscope;

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

		public int getHoroscope() {
			return horoscope;
		}

		public void setHoroscope(int horoscope) {
			this.horoscope = horoscope;
		}

	}

}
