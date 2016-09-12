package cn.chono.yopper.data;

import java.io.Serializable;
import java.util.List;

public class DiscoverPeopleDto implements Serializable {

	private String nextQuery;

	private List<NearPeopleDto> list;

	public String getNextQuery() {
		return nextQuery;
	}

	public void setNextQuery(String nextQuery) {
		this.nextQuery = nextQuery;
	}

	public List<NearPeopleDto> getList() {
		return list;
	}

	public void setList(List<NearPeopleDto> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "DiscoverPeopleDto [nextQuery=" + nextQuery + ", list=" + list
				+ "]";
	}

}
