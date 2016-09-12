package cn.chono.yopper.data;

import cn.chono.yopper.smack.entity.EntityBase;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "PeopleFilterDto")
public class PeopleFilterDto extends EntityBase {

	@Column(column = "UserId")
	private int UserId;
	@Column(column = "filterType")
	private int filterType;


	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public int getFilterType() {
		return filterType;
	}

	public void setFilterType(int filterType) {
		this.filterType = filterType;
	}
}
