package cn.chono.yopper.data;

import java.util.List;

/**
 * 约会刘列表对象
 * 
 */
public class DatingUserListDto {

	private String  nextQuery;
	private int start;

	private List<AppointOwner> list;



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

	
	public List<AppointOwner> getList() {
		return list;
	}
	
	

	public void setList(List<AppointOwner> list) {
		this.list = list;
	}


	
	
	
}
