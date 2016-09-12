package cn.chono.yopper.entity.likeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianghua on 2016/7/25.
 */
public class IlikeBase implements Serializable {

    /**
     * nextQuery : sample string 1
     * list : [{"userInfo":{"id":1,"uid":"sample string 2","mobile":"sample string 3","hashedPassword":"sample string 4","name":"sample string 5","horoscope":0,"headImg":"sample string 6","headImgInternal":"sample string 7","sex":0,"status":0,"level":0,"regTime":"2016-07-25T09:56:22.1502887+08:00","cityCode":8},"isUnlock":true,"isActivityExpert":true,"currentUserPosition":0,"activity":{"activityId":"sample string 1","activityName":"sample string 2"},"dating":{"datingId":"sample string 1","datingName":"sample string 2"}},{"userInfo":{"id":1,"uid":"sample string 2","mobile":"sample string 3","hashedPassword":"sample string 4","name":"sample string 5","horoscope":0,"headImg":"sample string 6","headImgInternal":"sample string 7","sex":0,"status":0,"level":0,"regTime":"2016-07-25T09:56:22.1502887+08:00","cityCode":8},"isUnlock":true,"isActivityExpert":true,"currentUserPosition":0,"activity":{"activityId":"sample string 1","activityName":"sample string 2"},"dating":{"datingId":"sample string 1","datingName":"sample string 2"}},{"userInfo":{"id":1,"uid":"sample string 2","mobile":"sample string 3","hashedPassword":"sample string 4","name":"sample string 5","horoscope":0,"headImg":"sample string 6","headImgInternal":"sample string 7","sex":0,"status":0,"level":0,"regTime":"2016-07-25T09:56:22.1502887+08:00","cityCode":8},"isUnlock":true,"isActivityExpert":true,"currentUserPosition":0,"activity":{"activityId":"sample string 1","activityName":"sample string 2"},"dating":{"datingId":"sample string 1","datingName":"sample string 2"}}]
     * start : 2
     */

    private String nextQuery;
    private int start;
    /**
     * userInfo : {"id":1,"uid":"sample string 2","mobile":"sample string 3","hashedPassword":"sample string 4","name":"sample string 5","horoscope":0,"headImg":"sample string 6","headImgInternal":"sample string 7","sex":0,"status":0,"level":0,"regTime":"2016-07-25T09:56:22.1502887+08:00","cityCode":8}
     * isUnlock : true
     * isActivityExpert : true
     * currentUserPosition : 0
     * activity : {"activityId":"sample string 1","activityName":"sample string 2"}
     * dating : {"datingId":"sample string 1","datingName":"sample string 2"}
     */

    private List<ILike> list;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<ILike> getList() {
        return list;
    }

    public void setList(List<ILike> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "IlikeBase{" +
                "nextQuery='" + nextQuery + '\'' +
                ", start=" + start +
                ", list=" + list +
                '}';
    }
}
