package cn.chono.yopper.data;

import java.util.List;

import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailDto;

/**
 * Created by sunquan on 16/4/28.
 */
public class OrderListDto {

    private String nextQuery;

    private List<OrderDetailDto> list;

    private int start;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public List<OrderDetailDto> getList() {
        return list;
    }

    public void setList(List<OrderDetailDto> list) {
        this.list = list;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
