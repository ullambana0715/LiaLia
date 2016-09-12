package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name ="LoginVideoStatusDto")
public class LoginVideoStatusDto {

	@Column(column = "id")
	@NoAutoIncrement()
	private int id;
	
	@Column(column = "videoVerificationStatus")
	private int videoVerificationStatus;

	//0不显示 1显示红点
	@Column(column = "isVisible")
	private int isVisible;

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getVideoVerificationStatus() {
		return videoVerificationStatus;
	}

	public void setVideoVerificationStatus(int videoVerificationStatus) {
		this.videoVerificationStatus = videoVerificationStatus;
	}


	public int getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(int isVisible) {
		this.isVisible = isVisible;
	}
}
