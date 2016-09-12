package cn.chono.yopper.entity;


import java.util.List;

/**
 * Created by yangjinyu on 16/8/3.
 */
public class WithDrawRecordsEntity {

    private String nextQuery;

    private List<WithDrawItemEntity> list;

    private int start;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public List<WithDrawItemEntity> getList() {
        return list;
    }

    public void setList(List<WithDrawItemEntity> list) {
        this.list = list;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "WithDrawRecordsEntity{" +
                "nextQuery='" + nextQuery + '\'' +
                ", list=" + list +
                ", start=" + start +
                '}';
    }
}
