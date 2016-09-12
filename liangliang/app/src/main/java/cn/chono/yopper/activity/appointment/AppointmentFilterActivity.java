package cn.chono.yopper.activity.appointment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.base.App;
import cn.chono.yopper.event.DatingsFilterEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 约会条件筛选页面
 *
 * @author sam.sun
 */
public class AppointmentFilterActivity extends MainFrameActivity implements OnClickListener {

    private LayoutInflater mInflater;
    private View contextView;

    private TextView dating_filter_sort_new_tv;
    private TextView dating_filter_sort_date_tv;
    private TextView dating_filter_sort_distance_tv;

    private RadioGroup dating_filter_area_radiogroup;

    private RadioGroup dating_filter_sex_radiogroup;

    private RelativeLayout dating_filter_area_layout;

    private View dating_filter_area_line;

    private int sexType = 0;
    private int sortType = 0;
    private String firstArea = "";
    private String secondArea = "";

    private int appointType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        sexType = DbHelperUtils.getDbUserSex(LoginUser.getInstance().getUserId());

        if (sexType == 1) {
            sexType = 2;
        } else {
            sexType = 1;
        }

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            sortType = bundle.getInt("sortType");
            sexType = bundle.getInt("sexType");
            firstArea = bundle.getString("firstArea");
            secondArea = bundle.getString("secondArea");
            appointType = bundle.getInt("appointType");
        }
        initComponent();
        setSortView(sortType);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("约会筛选"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("约会筛选"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("筛选");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.VISIBLE);
        this.getBtnOption().setVisibility(View.GONE);
        this.gettvOption().setVisibility(View.VISIBLE);
        this.gettvOption().setText("完成");
        this.gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });


        this.getOptionLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                RxBus.get().post("DatingsFilterEvent", new DatingsFilterEvent(appointType, sexType, sortType, firstArea, secondArea));
                finish();
            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contextView = mInflater.inflate(R.layout.appointment_filter_layout, null);

        dating_filter_sort_new_tv = (TextView) contextView.findViewById(R.id.dating_filter_sort_new_tv);
        dating_filter_sort_new_tv.setOnClickListener(this);

        dating_filter_sort_date_tv = (TextView) contextView.findViewById(R.id.dating_filter_sort_date_tv);
        dating_filter_sort_date_tv.setOnClickListener(this);

        dating_filter_sort_distance_tv = (TextView) contextView.findViewById(R.id.dating_filter_sort_distance_tv);
        dating_filter_sort_distance_tv.setOnClickListener(this);

        dating_filter_area_layout = (RelativeLayout) contextView.findViewById(R.id.dating_filter_area_layout);
        dating_filter_area_line = contextView.findViewById(R.id.dating_filter_area_line);

        dating_filter_area_radiogroup = (RadioGroup) contextView.findViewById(R.id.dating_filter_area_radiogroup);

        dating_filter_area_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.dating_filter_area_all_radiobutton://全国
                        firstArea = "";
                        secondArea = "";
                        break;

                    case R.id.dating_filter_area_city_radiobutton://同城

                        LocInfo myLoc = Loc.getLoc();
                        if (myLoc != null && !CheckUtil.isEmpty(myLoc.getCity())) {
                            firstArea = myLoc.getProvince();
                            secondArea = myLoc.getCity();
                        }
                        break;

                }


            }
        });


        if (CheckUtil.isEmpty(firstArea) && CheckUtil.isEmpty(secondArea)) {
            dating_filter_area_radiogroup.check(R.id.dating_filter_area_all_radiobutton);
        } else {
            dating_filter_area_radiogroup.check(R.id.dating_filter_area_city_radiobutton);
        }


        dating_filter_sex_radiogroup = (RadioGroup) contextView.findViewById(R.id.dating_filter_sex_radiogroup);

        dating_filter_sex_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.dating_filter_sex_man_radiobutton://男
                        sexType = 1;
                        break;

                    case R.id.dating_filter_sex_women_radiobutton://女
                        sexType = 2;
                        break;

                    case R.id.dating_filter_sex_nolimit_radiobutton://不限
                        sexType = 0;
                        break;

                }


            }
        });

        if (sexType == 1) {
            dating_filter_sex_radiogroup.check(R.id.dating_filter_sex_man_radiobutton);
        } else if (sexType == 2) {
            dating_filter_sex_radiogroup.check(R.id.dating_filter_sex_women_radiobutton);
        } else {
            dating_filter_sex_radiogroup.check(R.id.dating_filter_sex_nolimit_radiobutton);
        }


        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.dating_filter_sort_new_tv:
                sortType = 0;
                setSortView(0);
                break;

            case R.id.dating_filter_sort_date_tv:
                sortType = 1;
                setSortView(1);
                break;

            case R.id.dating_filter_sort_distance_tv:
                sortType = 2;
                setSortView(2);
                break;

        }
    }


    private void setSortView(int sorttype) {

        switch (sorttype) {

            case 0:
                dating_filter_sort_new_tv.setBackgroundColor(getResources().getColor(R.color.color_ff7462));
                dating_filter_sort_new_tv.setTextColor(getResources().getColor(R.color.color_ffffff));

                dating_filter_sort_date_tv.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                dating_filter_sort_date_tv.setTextColor(getResources().getColor(R.color.color_333333));

                dating_filter_sort_distance_tv.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                dating_filter_sort_distance_tv.setTextColor(getResources().getColor(R.color.color_333333));

                dating_filter_area_layout.setVisibility(View.VISIBLE);

                dating_filter_area_line.setVisibility(View.VISIBLE);

                break;

            case 1:
                dating_filter_sort_new_tv.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                dating_filter_sort_new_tv.setTextColor(getResources().getColor(R.color.color_333333));

                dating_filter_sort_date_tv.setBackgroundColor(getResources().getColor(R.color.color_ff7462));
                dating_filter_sort_date_tv.setTextColor(getResources().getColor(R.color.color_ffffff));

                dating_filter_sort_distance_tv.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                dating_filter_sort_distance_tv.setTextColor(getResources().getColor(R.color.color_333333));

                dating_filter_area_layout.setVisibility(View.VISIBLE);

                dating_filter_area_line.setVisibility(View.VISIBLE);

                break;

            case 2:
                dating_filter_sort_new_tv.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                dating_filter_sort_new_tv.setTextColor(getResources().getColor(R.color.color_333333));

                dating_filter_sort_date_tv.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                dating_filter_sort_date_tv.setTextColor(getResources().getColor(R.color.color_333333));

                dating_filter_sort_distance_tv.setBackgroundColor(getResources().getColor(R.color.color_ff7462));
                dating_filter_sort_distance_tv.setTextColor(getResources().getColor(R.color.color_ffffff));

                dating_filter_area_layout.setVisibility(View.GONE);

                dating_filter_area_line.setVisibility(View.GONE);


                break;
        }
    }

}
