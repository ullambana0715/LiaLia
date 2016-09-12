package cn.chono.yopper.activity.appointment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.TravelLabelSetect;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.view.FlowLeftLayout;

/**
 * 旅行标签
 * Created by cc on 16/3/21.
 */
public class TravelLabelActivity extends MainFrameActivity {


    private FlowLeftLayout travel_label_travel_significance_lable_flowlayout;
    private FlowLeftLayout travel_label_like_preferences_lable_flowlayout;

    private List<TravelLabelSetect> significanceSetects;
    private List<TravelLabelSetect> preferencesSetects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.travel_label_activity);
        significanceSetects = new ArrayList<>();
        significanceSetects = (List<TravelLabelSetect>) getIntent().getExtras().getSerializable("significanceSetects");
        preferencesSetects = new ArrayList<>();
        preferencesSetects = (List<TravelLabelSetect>) getIntent().getExtras().getSerializable("preferencesSetects");

        initView();
        Resources res = getResources();
        String[] travel_significance_lable = res.getStringArray(R.array.travel_significance_lable);
        initLableSignificanceViews(travel_label_travel_significance_lable_flowlayout, travel_significance_lable);
        String[] like_preferences_lable = res.getStringArray(R.array.like_preferences_lable);
        initLablePreferencesSetectsViews(travel_label_like_preferences_lable_flowlayout, like_preferences_lable);


    }

    private void initView() {

        getTvTitle().setText(getString(R.string.text_travel_label));


        getBtnGoBack().setVisibility(View.VISIBLE);

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();

            }
        });


        travel_label_travel_significance_lable_flowlayout = (FlowLeftLayout) findViewById(R.id.travel_label_travel_significance_lable_flowlayout);

        travel_label_like_preferences_lable_flowlayout = (FlowLeftLayout) findViewById(R.id.travel_label_like_preferences_lable_flowlayout);


    }


    private void initLableSignificanceViews(FlowLeftLayout lableView, String[] lableList) {
        lableView.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 15;
        lp.rightMargin = 15;
        lp.topMargin = 15;
        lp.bottomMargin = 15;

        if (lableList != null && lableList.length > 0) {

            for (int i = 0; i < lableList.length; i++) {

                String context = lableList[i];

                TextView view = new TextView(this);
                view.setText(context);
                view.setTextSize(14);
                view.setPadding(40, 10, 40, 10);

                view.setGravity(Gravity.CENTER_HORIZONTAL);

                boolean ishave = false;

                if (significanceSetects != null && significanceSetects.size() > 0) {
                    for (int j = 0; j < significanceSetects.size(); j++) {
                        if (context.equals(significanceSetects.get(j).getContent())) {

                            ishave = true;

                            break;
                        }
                    }
                }


                if (ishave) {
                    view.setBackgroundResource(R.drawable.travel_label_lable_corners_setect);
                    view.setTextColor(getResources().getColor(R.color.color_ffffff));
                    view.setTag(new TravelLabelSetect(context, true));
                } else {
                    view.setBackgroundResource(R.drawable.travel_label_lable_corners_default);
                    view.setTextColor(getResources().getColor(R.color.color_333333));
                    view.setTag(new TravelLabelSetect(context, false));
                }


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TravelLabelSetect travelLabelSetect = (TravelLabelSetect) v.getTag();


                        if (travelLabelSetect.isSelect()) {

                            v.setTag(new TravelLabelSetect(travelLabelSetect.getContent(), false));
                            v.setBackgroundResource(R.drawable.travel_label_lable_corners_default);
                            TextView tvview = (TextView) v;
                            tvview.setTextColor(getResources().getColor(R.color.color_333333));
                            significanceChangeSelectedListData(new TravelLabelSetect(travelLabelSetect.getContent(), false));
                        } else {

                            if (significanceSetects.size() >= 3) {
                                DialogUtil.showDisCoverNetToast(TravelLabelActivity.this, "最多选择三个标签哦");
                            } else {
                                v.setTag(new TravelLabelSetect(travelLabelSetect.getContent(), true));
                                v.setBackgroundResource(R.drawable.travel_label_lable_corners_setect);
                                TextView tvview = (TextView) v;
                                tvview.setTextColor(getResources().getColor(R.color.color_ffffff));
                                significanceChangeSelectedListData(new TravelLabelSetect(travelLabelSetect.getContent(), true));
                            }
                        }
                    }
                });


                lableView.addView(view, lp);
            }
        }
    }

    private void initLablePreferencesSetectsViews(FlowLeftLayout lableView, String[] lableList) {
        lableView.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 15;
        lp.rightMargin = 15;
        lp.topMargin = 15;
        lp.bottomMargin = 15;

        if (lableList != null && lableList.length > 0) {

            for (int i = 0; i < lableList.length; i++) {

                String context = lableList[i];

                TextView view = new TextView(this);
                view.setText(context);
                view.setTextSize(14);
                view.setPadding(40, 10, 40, 10);
//                view.setPadding(35, 10, 35, 10);
                view.setGravity(Gravity.CENTER_HORIZONTAL);


                boolean ishave = false;

                if (preferencesSetects != null && preferencesSetects.size() > 0) {
                    for (int j = 0; j < preferencesSetects.size(); j++) {
                        if (context.equals(preferencesSetects.get(j).getContent())) {

                            ishave = true;

                            break;
                        }
                    }
                }


                if (ishave) {
                    view.setBackgroundResource(R.drawable.travel_label_lable_corners_setect);
                    view.setTextColor(getResources().getColor(R.color.color_ffffff));
                    view.setTag(new TravelLabelSetect(context, true));
                } else {
                    view.setBackgroundResource(R.drawable.travel_label_lable_corners_default);
                    view.setTextColor(getResources().getColor(R.color.color_333333));
                    view.setTag(new TravelLabelSetect(context, false));
                }


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TravelLabelSetect travelLabelSetect = (TravelLabelSetect) v.getTag();


                        if (travelLabelSetect.isSelect()) {

                            v.setTag(new TravelLabelSetect(travelLabelSetect.getContent(), false));
                            v.setBackgroundResource(R.drawable.travel_label_lable_corners_default);
                            TextView tvview = (TextView) v;
                            tvview.setTextColor(getResources().getColor(R.color.color_333333));
                            preferencesChangeSelectedListData(new TravelLabelSetect(travelLabelSetect.getContent(), false));
                        } else {

                            if (preferencesSetects.size() >= 3) {
                                DialogUtil.showDisCoverNetToast(TravelLabelActivity.this, "最多选择三个标签哦");
                            } else {
                                v.setTag(new TravelLabelSetect(travelLabelSetect.getContent(), true));
                                v.setBackgroundResource(R.drawable.travel_label_lable_corners_setect);
                                TextView tvview = (TextView) v;
                                tvview.setTextColor(getResources().getColor(R.color.color_ffffff));
                                preferencesChangeSelectedListData(new TravelLabelSetect(travelLabelSetect.getContent(), true));
                            }
                        }
                    }
                });


                lableView.addView(view, lp);
            }
        }
    }

    private void significanceChangeSelectedListData(TravelLabelSetect travelLabelSetect) {


        if (travelLabelSetect.isSelect()) {
            significanceSetects.add(travelLabelSetect);
        } else {
            for (int i = 0; i < significanceSetects.size(); i++) {
                if (travelLabelSetect.getContent().equals(significanceSetects.get(i).getContent())) {
                    significanceSetects.remove(i);
                    break;
                }
            }

        }
    }

    private void preferencesChangeSelectedListData(TravelLabelSetect travelLabelSetect) {


        if (travelLabelSetect.isSelect()) {
            preferencesSetects.add(travelLabelSetect);
        } else {
            for (int i = 0; i < preferencesSetects.size(); i++) {
                if (travelLabelSetect.getContent().equals(preferencesSetects.get(i).getContent())) {
                    preferencesSetects.remove(i);
                    break;
                }
            }

        }
    }


    @Override
    public void finish() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("significanceSetects", (Serializable) significanceSetects);
        bundle.putSerializable("preferencesSetects", (Serializable) preferencesSetects);
        Intent intent = new Intent(TravelLabelActivity.this, TravelActivity.class);
        intent.putExtras(bundle);
        setResult(YpSettings.TRAVE_LABEL, intent);

        super.finish();
    }
}
