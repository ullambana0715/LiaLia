package cn.chono.yopper.data;

import java.io.Serializable;

public class Wish implements Serializable{

	private static final long serialVersionUID = 1L;
	private int datingType;
	private String content;
	private double lat;
	private double lng;
	private String createTime;
	private int locationId;
	private String datingTypeImageUrl;
	private String datingTypeName;

	private int id;
	
	private int userId;
	

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getDatingTypeImageUrl() {
		return datingTypeImageUrl;
	}

	public void setDatingTypeImageUrl(String datingTypeImageUrl) {
		this.datingTypeImageUrl = datingTypeImageUrl;
	}

	public String getDatingTypeName() {
		return datingTypeName;
	}

	public void setDatingTypeName(String datingTypeName) {
		this.datingTypeName = datingTypeName;
	}

	public int getDatingType() {
		return datingType;
	}

	public void setDatingType(int datingType) {
		this.datingType = datingType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

}
