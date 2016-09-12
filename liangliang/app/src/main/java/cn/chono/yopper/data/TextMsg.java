package cn.chono.yopper.data;

public class TextMsg {

	private String type;

	private String text;

	private int counsel;

	private String dateid;

	//0 -false  1-true
	private int mask;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}


	public int getCounsel() {
		return counsel;
	}

	public void setCounsel(int counsel) {
		this.counsel = counsel;
	}

	public String getDateid() {
		return dateid;
	}

	public void setDateid(String dateid) {
		this.dateid = dateid;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public TextMsg(String type, String text, int counsel, String dateid, int mask) {
		super();
		this.type = type;
		this.text = text;
		this.counsel = counsel;
		this.dateid = dateid;
		this.mask = mask;
	}

	public TextMsg() {
	}
}
