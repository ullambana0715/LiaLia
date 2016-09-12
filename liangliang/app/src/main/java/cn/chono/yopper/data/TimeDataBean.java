package cn.chono.yopper.data;

import java.util.List;

/**
 * Created by cc on 16/3/28.
 */
public class TimeDataBean {

    private String DaysMonth;

    private List<TimeDates> timeDatas;

    public String getDaysMonth() {
        return DaysMonth;
    }

    public void setDaysMonth(String daysMonth) {
        DaysMonth = daysMonth;
    }

    public List<TimeDates> getTimeDatas() {
        return timeDatas;
    }

    public void setTimeDatas(List<TimeDates> timeDatas) {
        this.timeDatas = timeDatas;
    }


    @Override
    public String toString() {
        return "TimeDataBean{" +
                "DaysMonth='" + DaysMonth + '\'' +
                ", timeDatas=" + timeDatas +
                '}';
    }
}
