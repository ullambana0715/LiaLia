package cn.chono.yopper.data;

public class AttractionResultDto {
	
	
	
	private int resultCode;
	private int attractionAdded;
	private int pointsCost;

	private String from;
	private String type;

	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public int getAttractionAdded() {
		return attractionAdded;
	}
	public void setAttractionAdded(int attractionAdded) {
		this.attractionAdded = attractionAdded;
	}
	public int getPointsCost() {
		return pointsCost;
	}
	public void setPointsCost(int pointsCost) {
		this.pointsCost = pointsCost;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
