package cn.chono.yopper.data;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

public class UserBubblingLocalDto extends EntityBase implements Serializable {

	@Column(column = "data")
	private String data;

	@Column(column = "userId")
	@NoAutoIncrement()
	private int UserId;

	@Column(column = "time")
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	@Override
	public String toString() {
		return "DiscoverBubblingLocalDto [data=" + data + ", UserId=" + UserId
				+ ", time=" + time + "]";
	}

	
}
