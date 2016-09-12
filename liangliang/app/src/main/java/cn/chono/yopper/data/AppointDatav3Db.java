package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 用来数据库存储附近约会的数据
 * @author SQ
 *
 */
@Table(name ="AppointDatav3Db")
public class AppointDatav3Db {

	@Column(column = "id")
	@NoAutoIncrement()
	private int id;
	
	@Column(column = "appointListStr")
	private String appointListStr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppointListStr() {
		return appointListStr;
	}

	public void setAppointListStr(String appointListStr) {
		this.appointListStr = appointListStr;
	}
}
