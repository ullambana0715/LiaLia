package cn.chono.yopper.data;

import cn.chono.yopper.smack.entity.EntityBase;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;


@Table(name ="BaseUser")

public class BaseUser {
	
	@Column(column = "id")
	@NoAutoIncrement()
	private int id;
	
	@Column(column = "name")
	private String name;
	
	@Column(column = "horoscope")
	private int horoscope;
	
	@Column(column = "headImg")
	private String headImg;

	@Column(column = "sex")
	private int sex;
	//"regTime": "2015-04-02T11:42:19.4435569+08:00"
	
	@Column(column = "regTime")
	private String regTime;
	
	
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
	
	
	
	public BaseUser(int id, String name, int horoscope, String headImg,
			int sex, String regTime) {
		super();
		this.id = id;
		this.name = name;
		this.horoscope = horoscope;
		this.headImg = headImg;
		this.sex = sex;
		this.regTime = regTime;
	}
	
	
	public BaseUser() {
		super();
	}



}
