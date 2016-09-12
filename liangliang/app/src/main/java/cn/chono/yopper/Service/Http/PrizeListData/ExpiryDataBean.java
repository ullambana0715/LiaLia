package cn.chono.yopper.Service.Http.PrizeListData;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by jianghua on 2016/3/15.
 */
public class ExpiryDataBean extends ParameterBean {

    //结果集偏移量，从0开始计算；默认为0
    private String start;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
