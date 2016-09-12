package cn.chono.yopper.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name ="AppointFilterDto")
public class AppointFilterDto {
	
	@Column(column = "id")
	@NoAutoIncrement()
	private int id;
	
	@Column(column = "datingType")
	private int datingType;
	
	@Column(column = "sexType")
	private int sexType;
	
	@Column(column = "sortType")
	private int sortType;

	@Column(column = "firstArea")
	private String firstArea;


	@Column(column = "secondArea")
	private String secondArea;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDatingType() {
		return datingType;
	}

	public void setDatingType(int datingType) {
		this.datingType = datingType;
	}

	public int getSexType() {
		return sexType;
	}

	public void setSexType(int sexType) {
		this.sexType = sexType;
	}

	public AppointFilterDto() {
		super();
	}

	public int getSortType() {
		return sortType;
	}

	public void setSortType(int sortType) {
		this.sortType = sortType;
	}


	public String getFirstArea() {
		return firstArea;
	}

	public void setFirstArea(String firstArea) {
		this.firstArea = firstArea;
	}

	public String getSecondArea() {
		return secondArea;
	}

	public void setSecondArea(String secondArea) {
		this.secondArea = secondArea;
	}
}
