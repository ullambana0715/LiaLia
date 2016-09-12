package cn.chono.yopper.Service.Http.Banners;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cc on 16/2/23.
 */
public class BannersDto implements Serializable {

    private int rows;

    private int start;

    private int total;


    private List<BannersData> list;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BannersData> getList() {
        return list;
    }

    public void setList(List<BannersData> list) {
        this.list = list;
    }
}
