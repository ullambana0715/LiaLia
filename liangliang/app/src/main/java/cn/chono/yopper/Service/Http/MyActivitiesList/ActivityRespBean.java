package cn.chono.yopper.Service.Http.MyActivitiesList;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.data.Activities;

/**
 * Created by jianghua on 2016/6/29.
 */
public class ActivityRespBean implements Serializable {
    private String nextQuery;
    private List<Activities> list;
    private int start;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public List<Activities> getList() {
        return list;
    }

    public void setList(List<Activities> list) {
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
        return "ActivityRespBean{" +
                "nextQuery='" + nextQuery + '\'' +
                ", list=" + list +
                ", start=" + start +
                '}';
    }
}
