package cn.chono.yopper.data;

public class DatingAttamptDto {

	private String type;

	private String content;

	private int datingType;
	private String datingTypeImgUrl;

	private String datingTypeName ;
	private LocationDto location;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getDatingType() {
		return datingType;
	}
	public void setDatingType(int datingType) {
		this.datingType = datingType;
	}
	public String getDatingTypeImgUrl() {
		return datingTypeImgUrl;
	}
	public void setDatingTypeImgUrl(String datingTypeImgUrl) {
		this.datingTypeImgUrl = datingTypeImgUrl;
	}
	public String getDatingTypeName() {
		return datingTypeName;
	}
	public void setDatingTypeName(String datingTypeName) {
		this.datingTypeName = datingTypeName;
	}
	public LocationDto getLocation() {
		return location;
	}
	public void setLocation(LocationDto location) {
		this.location = location;
	}
	
	
	
}
