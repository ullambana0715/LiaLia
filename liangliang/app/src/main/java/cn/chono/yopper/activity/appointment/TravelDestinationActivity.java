package cn.chono.yopper.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingsTravelConfigs.DatingsTravelConfigsResp;
import cn.chono.yopper.Service.Http.DatingsTravelConfigs.DatingsTravelConfigsService;
import cn.chono.yopper.Service.Http.DatingsTravelConfigs.TravelConfigs;
import cn.chono.yopper.Service.Http.DatingsTravelConfigs.TravelConfigsData;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BaseUser;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.view.FlowLeftLayout;

/**
 * 旅行目的地
 * Created by cc on 16/3/21.
 */
public class TravelDestinationActivity extends MainFrameActivity {

    private LinearLayout travel_label_back_container;
    private LinearLayout travel_label_btnOption_containers;
    private TextView travel_label_tvOption, error_network_tv;


    private int type;

    private LinearLayout travel_destination_context_layout, error_network_layout;

    private String frompage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.travel_destination_activity);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("type")) {
            type = bundle.getInt("type");
        }

        if (bundle.containsKey(YpSettings.FROM_PAGE))
            frompage = bundle.getString(YpSettings.FROM_PAGE);

        initView();
        setTvOption();
        lableData();

    }

    private void initView() {

        getTitleLayout().setVisibility(View.GONE);

        travel_label_back_container = (LinearLayout) findViewById(R.id.travel_label_back_container);
        travel_label_btnOption_containers = (LinearLayout) findViewById(R.id.travel_label_btnOption_containers);
        travel_label_tvOption = (TextView) findViewById(R.id.travel_label_tvOption);


        travel_destination_context_layout = (LinearLayout) findViewById(R.id.travel_destination_context_layout);
        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);

        error_network_tv = (TextView) findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lableData();
            }
        });


        travel_label_back_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        travel_label_btnOption_containers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String context = (String) travel_label_tvOption.getText();
                setResults(context);

            }
        });

    }


    private void setTvOption() {

        int userid = LoginUser.getInstance().getUserId();

        BaseUser baseUser = DbHelperUtils.getBaseUser(userid);

        if (null == baseUser) {
            return;

        }

        int sex = baseUser.getSex();
        if (1 == sex) {
            travel_label_tvOption.setText(getString(R.string.discuss_decide));
            return;
        }

        travel_label_tvOption.setText(getString(R.string.where_to_go));
    }

    private void lableData() {
        DatingsTravelConfigsService travelConfigsService = new DatingsTravelConfigsService(this);

        travelConfigsService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DatingsTravelConfigsResp datingsTravelConfigsResp = (DatingsTravelConfigsResp) respBean;

                TravelConfigsData travelConfigsData = datingsTravelConfigsResp.getResp();

                if (null == travelConfigsData) return;


                List<TravelConfigs> travelConfigsList = travelConfigsData.getTravelConfigs();

                if (null != travelConfigsList && travelConfigsList.size() > 0) {//有数据

                    travel_destination_context_layout.setVisibility(View.VISIBLE);
                    error_network_layout.setVisibility(View.GONE);

                    for (int i = 0; i < travelConfigsList.size(); i++) {

                        contextLayout(travelConfigsList.get(i));
                    }

                    return;
                }
                //无数据

                travel_destination_context_layout.setVisibility(View.GONE);

                error_network_layout.setVisibility(View.VISIBLE);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                travel_destination_context_layout.setVisibility(View.GONE);

                error_network_layout.setVisibility(View.VISIBLE);


            }
        });

        travelConfigsService.enqueue();
    }

    private void contextLayout(TravelConfigs travelConfigs) {

        LinearLayout travel_destination_time_layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.travel_destination_time_layout, null);

        TextView travel_destination_title_tv = (TextView) travel_destination_time_layout.findViewById(R.id.travel_destination_title_tv);

        FlowLeftLayout flowLeftLayout = (FlowLeftLayout) travel_destination_time_layout.findViewById(R.id.travel_destination_lable_flowlayout);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        travel_destination_context_layout.addView(travel_destination_time_layout);

        if (null != travelConfigs && !TextUtils.isEmpty(travelConfigs.getCategory()))
            travel_destination_title_tv.setText(travelConfigs.getCategory());


        if (null != travelConfigs) initLableViews(flowLeftLayout, travelConfigs.getItemNames());


    }


    private void initLableViews(FlowLeftLayout lableLayout, String[] lableList) {
        lableLayout.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 14;
        lp.rightMargin = 14;
        lp.topMargin = 14;
        lp.bottomMargin = 14;


        if (lableList != null && lableList.length > 0) {

            for (int i = 0; i < lableList.length; i++) {
                TextView view = new TextView(this);
                view.setPadding(20, 10, 20, 10);
                view.setText(lableList[i]);
                view.setTextSize(16);

                view.setTextColor(getResources().getColor(R.color.travel_destination_lable_tv_style));
                view.setBackgroundResource(R.drawable.travel_destination_lable_style);
                lableLayout.addView(view, lp);

                view.setTag(lableList[i]);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String context = (String) v.getTag();

                        setResults(context);

                    }
                });
            }
        }
    }

    private void setResults(String context) {
        Bundle bundle = new Bundle();
        bundle.putString("content", context);

        bundle.putString(YpSettings.FROM_PAGE, frompage);

        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtras(bundle);

        if (type == 0) {

            startActivity(intent);

        } else {
            setResult(YpSettings.TRAVE_DESTINATION, intent);
        }


        finish();
    }
}
