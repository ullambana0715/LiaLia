package cn.chono.yopper.data;

import java.util.List;

/**
 * 访客列表对象
 * 
 */
public class VisitorsListDto {

	private String  nextQuery;
	private int start;

	private List<VisitorsDto> list;



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

	
	public List<VisitorsDto> getList() {
		return list;
	}
	
	

	public void setList(List<VisitorsDto> list) {
		this.list = list;
	}


	
	
	
}
