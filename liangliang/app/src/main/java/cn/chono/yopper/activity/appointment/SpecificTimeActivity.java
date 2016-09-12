package cn.chono.yopper.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.SpecificTimeAdapter;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.data.TimeDataBean;
import cn.chono.yopper.data.TimeDates;
import cn.chono.yopper.utils.TimeUtil;

/**
 * 日历
 * Created by cc on 16/3/28.
 */
public class SpecificTimeActivity extends MainFrameActivity implements SpecificTimeAdapter.OnItemClickListener {


    private RecyclerView specific_time_recyclerView;

    private SpecificTimeAdapter adapter;

    private int isData;


    private int mDays;

    private int mMonth;

    private int mDate;

    private int APPOINT_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.specific_time_activity);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.APPOINTMENT_INTENT_YTPE))
            APPOINT_TYPE = bundle.getInt(YpSettings.APPOINTMENT_INTENT_YTPE);

        if (bundle.containsKey("days"))
            mDays = bundle.getInt("days");
        if (bundle.containsKey("month"))
            mMonth = bundle.getInt("month");
        if (bundle.containsKey("Date"))
            mDate = bundle.getInt("Date");

        initView();
    }

    private void initView() {


        getTvTitle().setText("选择日期");

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        specific_time_recyclerView = (RecyclerView) findViewById(R.id.specific_time_recyclerView);
        specific_time_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SpecificTimeAdapter(this, mDays, mMonth, mDate);
        adapter.setOnItemClickListener(this);
        adapter.setData(initData());
        specific_time_recyclerView.setAdapter(adapter);
    }


    private List<TimeDataBean> initData() {

        int days = TimeUtil.getDays();
        int month = TimeUtil.getMonth();
        int date = TimeUtil.getDate();

        int DaysOfMonth = getDaysOfMonth(days, month);//取得某个月有多少天

        int week = getWeek(days, month, 1);//根据星期几在数据里添置空数据

        List<TimeDates> timeDateList1 = setListdata(days, month, date, week, DaysOfMonth);//当月的数据


        TimeDataBean timeDataBean1 = new TimeDataBean();

        timeDataBean1.setDaysMonth(getDaysMonth(days, month));

        timeDataBean1.setTimeDatas(timeDateList1);


        //设置下个月的数据


        int days2;
        int month2;
        int date2;

        if (12 == month) {//当年为12月时。下个月为一月,年也为下一年
            days2 = days + 1;
            month2 = 1;
            date2 = 1;

        } else {
            days2 = days;
            month2 = month + 1;
            date2 = 1;
        }

        int DaysOfMonth2 = getDaysOfMonth(days2, month2);//取得某个月有多少天

        int week2 = getWeek(days2, month2, 1);//根据星期几在数据里添置空数据

        List<TimeDates> timeDateList2 = setListdata(days2, month2, date2, week2, DaysOfMonth2);//当月的数据

        TimeDataBean timeDataBean2 = new TimeDataBean();

        timeDataBean2.setDaysMonth(getDaysMonth(days2, month2));

        timeDataBean2.setTimeDatas(timeDateList2);

        List<TimeDataBean> list = new ArrayList<>();

        list.add(timeDataBean1);
        list.add(timeDataBean2);

        LogUtils.e(list.toString());

        return list;

    }


    private List<TimeDates> setListdata(int days, int month, int day, int week, int DaysOfMonth) {

        LogUtils.e(days + "");
        LogUtils.e(month + "");
        LogUtils.e(day + "");
        LogUtils.e(week + "");
        LogUtils.e(DaysOfMonth + "");


        List<TimeDates> timeDateList = new ArrayList<>();


        for (int a = 0; a < week; a++) {
            timeDateList.add(new TimeDates());
        }


        int t_days = TimeUtil.getDays();
        int t_month = TimeUtil.getMonth();
        int t_date = TimeUtil.getDate();


        LogUtils.e("t_days" + t_days);
        LogUtils.e("t_month" + t_days);
        LogUtils.e("t_date" + t_days);


        for (int i = 0; i < DaysOfMonth; i++) {

            int dayt = i + 1;

            LogUtils.e("dayt" + dayt);


            TimeDates timeDates1 = new TimeDates();

            timeDates1.setDays(days);

            timeDates1.setMonth(month);

            timeDates1.setDate(dayt);


            if (t_month != month && i < isData) {
                timeDates1.setData(true);
            }

            if (t_month == month && dayt >= t_date) {
                isData++;
                timeDates1.setData(true);
            }

            LogUtils.e("isData" + isData);


            if (t_days == days && t_month == month && t_date == dayt) {

                timeDates1.setType("今天");
            } else if (t_days == days && t_month == month && t_date + 1 == dayt) {
                timeDates1.setType("明天");
            } else if (t_days == days && t_month == month && t_date + 2 == dayt) {
                timeDates1.setType("后天");
            }


            timeDateList.add(timeDates1);


        }

        isData = 30 - isData;
        LogUtils.e("isData" + isData);

        int timeDateListSize = timeDateList.size();

        LogUtils.e("timeDateListSize" + timeDateListSize);

        int leight = 35 - timeDateListSize;

        LogUtils.e("leight" + leight);


        for (int j = 0; j < leight; j++) {
            timeDateList.add(new TimeDates());
        }

        return timeDateList;
    }

    private String getDaysMonth(int days, int month) {

        StringBuffer sb = new StringBuffer();

        sb.append(days);

        sb.append("年");

        sb.append(month);

        sb.append("月");
        return sb.toString();
    }

    private int getDaysOfMonth(int days, int month) {
        return TimeUtil.getDaysOfMonth(days, month);
    }

    private int getWeek(int year, int month, int day) {
        String week = TimeUtil.getWeek(year, month, day);

        int addDate = 0;

        if (TextUtils.equals("星期日", week)) {
            addDate = 0;
        } else if (TextUtils.equals("星期一", week)) {
            addDate = 1;
        } else if (TextUtils.equals("星期二", week)) {
            addDate = 2;
        } else if (TextUtils.equals("星期三", week)) {
            addDate = 3;
        } else if (TextUtils.equals("星期四", week)) {
            addDate = 4;
        } else if (TextUtils.equals("星期五", week)) {
            addDate = 5;
        } else if (TextUtils.equals("星期六", week)) {
            addDate = 6;
        }
        return addDate;

    }


    @Override
    public void onItemListener(int position, Object object) {
        TimeDates timeDates = (TimeDates) object;

        if (null != timeDates) {

            Intent timeIt = new Intent();

            switch (APPOINT_TYPE) {
                case Constant.APPOINT_TYPE_TRAVEL:

                    timeIt.setClass(SpecificTimeActivity.this, TravelActivity.class);

                    break;
                default:
                    timeIt.setClass(SpecificTimeActivity.this, ReleaseAppointmentActivity.class);

                    break;
            }


            Bundle timeItBd = new Bundle();


            timeItBd.putInt("days", timeDates.getDays());
            timeItBd.putInt("month", timeDates.getMonth());
            timeItBd.putInt("Date", timeDates.getDate());

            timeIt.putExtras(timeItBd);

            setResult(YpSettings.MOVEMENT_TIME, timeIt);


        }

        finish();

    }
}
