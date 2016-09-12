package cn.chono.yopper.data;

import java.util.List;

public class LikeListDto {

    private String nextQuery;
    private List<LikeDto> list;
    private int start;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public List<LikeDto> getList() {
        return list;
    }

    public void setList(List<LikeDto> list) {
        this.list = list;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }


}
