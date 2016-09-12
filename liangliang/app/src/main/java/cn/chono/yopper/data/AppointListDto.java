package cn.chono.yopper.data;

import java.util.List;

/**
 * 约会刘列表对象
 * 
 */
public class AppointListDto {

	private String  nextQuery;
	private int start;

	private List<AppointmentDto> list;



	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}


	public String getNextQuery() {
		return nextQuery;
	}

	
	public void setNextQuery(String nextQuery) {
		this.nextQuery = nextQuery;
	}

	
	public List<AppointmentDto> getList() {
		return list;
	}
	
	

	public void setList(List<AppointmentDto> list) {
		this.list = list;
	}


	
	
	
}
