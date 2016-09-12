package cn.chono.yopper.data;

import java.io.Serializable;

public class NearLoc implements Serializable {

	// 地点id
	private Integer id;
	// 地点经纬度
	private double lat;
	private double lng;
	// 地点名
	private String name;
	// 城市
	private int city;
	// 地址
	private String address;

	private boolean isFeatured;
	private int status;
	private int[] activityTypes;
	private int type;

	private String typeImgUrl;

	private int score;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isFeatured() {
		return isFeatured;
	}

	public void setFeatured(boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int[] getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(int[] activityTypes) {
		this.activityTypes = activityTypes;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeImgUrl() {
		return typeImgUrl;
	}

	public void setTypeImgUrl(String typeImgUrl) {
		this.typeImgUrl = typeImgUrl;
	}

}
