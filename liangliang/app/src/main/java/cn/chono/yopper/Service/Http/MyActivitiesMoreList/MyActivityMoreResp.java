package cn.chono.yopper.Service.Http.MyActivitiesMoreList;

import java.io.Serializable;
import java.util.List;

import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.data.Activities;

/**
 * Created by jianghua on 2016/6/13.
 */
public class MyActivityMoreResp extends RespBean {
    private ActivityRespBean resp;

    public ActivityRespBean getResp() {
        return resp;
    }

    public void setResp(ActivityRespBean resp) {
        this.resp = resp;
    }

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
    }
}
