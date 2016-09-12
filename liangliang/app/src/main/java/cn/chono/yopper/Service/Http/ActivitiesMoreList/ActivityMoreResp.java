package cn.chono.yopper.Service.Http.ActivitiesMoreList;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.IndexActivities;

/**
 * Created by jianghua on 2016/6/14.
 */
public class ActivityMoreResp extends RespBean {
    private IndexActivitiesResp resp;

    public IndexActivitiesResp getResp() {
        return resp;
    }

    public void setResp(IndexActivitiesResp resp) {
        this.resp = resp;
    }

    public class IndexActivitiesResp implements Serializable {
        private String nextQuery;
        private List<IndexActivities> list;
        private int start;

        public String getNextQuery() {
            return nextQuery;
        }

        public void setNextQuery(String nextQuery) {
            this.nextQuery = nextQuery;
        }

        public List<IndexActivities> getList() {
            return list;
        }

        public void setList(List<IndexActivities> list) {
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
            return "IndexActivitiesResp{" +
                    "nextQuery='" + nextQuery + '\'' +
                    ", list=" + list +
                    ", start=" + start +
                    '}';
        }
    }
}
