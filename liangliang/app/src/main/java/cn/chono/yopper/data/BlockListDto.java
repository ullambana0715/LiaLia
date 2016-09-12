package cn.chono.yopper.data;

import java.util.List;

public class BlockListDto {

	private String nextQuery;
	private List<BlockDto> list;
	private int start;
	
	public String getNextQuery() {
		return nextQuery;
	}
	public void setNextQuery(String nextQuery) {
		this.nextQuery = nextQuery;
	}
	public List<BlockDto> getList() {
		return list;
	}
	public void setList(List<BlockDto> list) {
		this.list = list;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
	
	
	
}
