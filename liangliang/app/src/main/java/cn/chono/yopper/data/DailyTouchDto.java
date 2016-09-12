package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name ="DailyTouchDto")

public class DailyTouchDto {

	@Column(column = "id")
	@NoAutoIncrement()
	private int id;
	
	@Column(column = "time")
	private long time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public DailyTouchDto(int id, long time) {
		super();
		this.id = id;
		this.time = time;
	}
	
	
	public DailyTouchDto() {
		super();
	}
	
	
}
