package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import cn.chono.yopper.smack.entity.*;

/**
 * 
 * 用于数据库缓存咨询师信息
 * @author SQ
 *
 */

@Table(name ="CounselInfoTable")
public class CounselInfoTable {

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


	public CounselInfoTable(int id, String resp) {
		this.id = id;
		this.resp = resp;
	}

	public CounselInfoTable() {
		super();
	}
}
