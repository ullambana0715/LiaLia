package cn.chono.yopper.entity.charm;

import java.util.List;

/**
 * Created by sunquan on 16/8/5.
 */
public class ReceiveGiftListEntity {

    private String nextQuery;

    private List<ReceiveGiftInfoEntity> list;

    private int start;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public List<ReceiveGiftInfoEntity> getList() {
        return list;
    }

    public void setList(List<ReceiveGiftInfoEntity> list) {
        this.list = list;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
