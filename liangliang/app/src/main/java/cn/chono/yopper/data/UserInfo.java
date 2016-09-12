package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 *
 * 用于数据库缓存用户的个人信息和相册信息
 * @author SQ
 *
 */

@Table(name ="UserInfo")
public class UserInfo {
	
	@Column(column = "id")
	@NoAutoIncrement()
	private int id;
	
	@Column(column = "resp")
	private String resp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}
	
	
	
	

	public UserInfo() {
		super();
	}

	public UserInfo(int id, String resp) {
		super();
		this.id = id;
		this.resp = resp;
	}
	
	
	
	
}
