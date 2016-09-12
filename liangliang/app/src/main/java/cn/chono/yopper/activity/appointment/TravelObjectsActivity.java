package cn.chono.yopper.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.TravelObjects;

/**
 * 旅行对象
 * Created by cc on 16/3/18.
 */
public class TravelObjectsActivity extends MainFrameActivity implements View.OnClickListener {


    private LinearLayout travel_objects_tyrant_m_layout;
    private ImageView travel_objects_tyrant_m_iv;
    private ImageView travel_objects_tyrant_m_select_iv;

    private LinearLayout travel_objects_overbearing_chairman_layout;
    private ImageView travel_objects_overbearing_chairman_iv;
    private ImageView travel_objects_overbearing_chairman_select_iv;

    private LinearLayout travel_objects_rich_handsome_layout;
    private ImageView travel_objects_rich_handsome_iv;
    private ImageView travel_objects_rich_handsome_select_iv;

    private LinearLayout travel_objects_female_temperament_layout;
    private ImageView travel_objects_female_temperament_iv;
    private ImageView travel_objects_female_temperament_select_iv;

    private LinearLayout travel_objects_silly_sweet_white_layout;
    private ImageView travel_objects_silly_sweet_white_iv;
    private ImageView travel_objects_silly_sweet_white_select_iv;

    private LinearLayout travel_objects_phu_bai_layout;
    private ImageView travel_objects_phu_bai_iv;
    private ImageView travel_objects_phu_bai_select_iv;


    private TravelObjects mTravelObjects;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.travel_objects_activity);

        mTravelObjects = (TravelObjects) getIntent().getExtras().getSerializable("mTravelObjects");

        initView();

        setSelectView(mTravelObjects);
    }

    private void initView() {

        getTvTitle().setText(getString(R.string.text_invite_objects));

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();

            }
        });


        travel_objects_tyrant_m_layout = (LinearLayout) findViewById(R.id.travel_objects_tyrant_m_layout);
        travel_objects_tyrant_m_iv = (ImageView) findViewById(R.id.travel_objects_tyrant_m_iv);
        travel_objects_tyrant_m_select_iv = (ImageView) findViewById(R.id.travel_objects_tyrant_m_select_iv);

        travel_objects_overbearing_chairman_layout = (LinearLayout) findViewById(R.id.travel_objects_overbearing_chairman_layout);
        travel_objects_overbearing_chairman_iv = (ImageView) findViewById(R.id.travel_objects_overbearing_chairman_iv);
        travel_objects_overbearing_chairman_select_iv = (ImageView) findViewById(R.id.travel_objects_overbearing_chairman_select_iv);

        travel_objects_rich_handsome_layout = (LinearLayout) findViewById(R.id.travel_objects_rich_handsome_layout);
        travel_objects_rich_handsome_iv = (ImageView) findViewById(R.id.travel_objects_rich_handsome_iv);
        travel_objects_rich_handsome_select_iv = (ImageView) findViewById(R.id.travel_objects_rich_handsome_select_iv);

        travel_objects_female_temperament_layout = (LinearLayout) findViewById(R.id.travel_objects_female_temperament_layout);
        travel_objects_female_temperament_iv = (ImageView) findViewById(R.id.travel_objects_female_temperament_iv);
        travel_objects_female_temperament_select_iv = (ImageView) findViewById(R.id.travel_objects_female_temperament_select_iv);

        travel_objects_silly_sweet_white_layout = (LinearLayout) findViewById(R.id.travel_objects_silly_sweet_white_layout);
        travel_objects_silly_sweet_white_iv = (ImageView) findViewById(R.id.travel_objects_silly_sweet_white_iv);
        travel_objects_silly_sweet_white_select_iv = (ImageView) findViewById(R.id.travel_objects_silly_sweet_white_select_iv);

        travel_objects_phu_bai_layout = (LinearLayout) findViewById(R.id.travel_objects_phu_bai_layout);
        travel_objects_phu_bai_iv = (ImageView) findViewById(R.id.travel_objects_phu_bai_iv);
        travel_objects_phu_bai_select_iv = (ImageView) findViewById(R.id.travel_objects_phu_bai_select_iv);

        travel_objects_tyrant_m_layout.setOnClickListener(this);
        travel_objects_overbearing_chairman_layout.setOnClickListener(this);
        travel_objects_rich_handsome_layout.setOnClickListener(this);
        travel_objects_female_temperament_layout.setOnClickListener(this);
        travel_objects_silly_sweet_white_layout.setOnClickListener(this);
        travel_objects_phu_bai_layout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.travel_objects_tyrant_m_layout://土豪男

                mTravelObjects = new TravelObjects(1, "土豪男");
                setSelectView(mTravelObjects);

                break;
            case R.id.travel_objects_overbearing_chairman_layout://霸道总裁

                mTravelObjects = new TravelObjects(2, "霸道总裁");
                setSelectView(mTravelObjects);
                break;
            case R.id.travel_objects_rich_handsome_layout://高富帅

                mTravelObjects = new TravelObjects(3, "高富帅");
                setSelectView(mTravelObjects);
                break;
            case R.id.travel_objects_female_temperament_layout://气质女

                mTravelObjects = new TravelObjects(4, "气质女");
                setSelectView(mTravelObjects);
                break;
            case R.id.travel_objects_silly_sweet_white_layout://傻白甜

                mTravelObjects = new TravelObjects(5, "傻白甜");
                setSelectView(mTravelObjects);
                break;
            case R.id.travel_objects_phu_bai_layout://白富美

                mTravelObjects = new TravelObjects(6, "白富美");
                setSelectView(mTravelObjects);
                break;
        }
    }

    private void setSelectView(TravelObjects mTravelObjects) {
        if (null == mTravelObjects) {
            return;
        }

        switch (mTravelObjects.getObjectsID()) {
            case 1:

                travel_objects_tyrant_m_iv.setBackgroundResource(R.drawable.objects_border_corners);
                travel_objects_overbearing_chairman_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_rich_handsome_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_female_temperament_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_silly_sweet_white_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_phu_bai_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);

                travel_objects_tyrant_m_select_iv.setVisibility(View.VISIBLE);
                travel_objects_overbearing_chairman_select_iv.setVisibility(View.GONE);
                travel_objects_rich_handsome_select_iv.setVisibility(View.GONE);
                travel_objects_female_temperament_select_iv.setVisibility(View.GONE);
                travel_objects_silly_sweet_white_select_iv.setVisibility(View.GONE);
                travel_objects_phu_bai_select_iv.setVisibility(View.GONE);


                break;
            case 2:

                travel_objects_tyrant_m_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_overbearing_chairman_iv.setBackgroundResource(R.drawable.objects_border_corners);
                travel_objects_rich_handsome_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_female_temperament_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_silly_sweet_white_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_phu_bai_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);

                travel_objects_tyrant_m_select_iv.setVisibility(View.GONE);
                travel_objects_overbearing_chairman_select_iv.setVisibility(View.VISIBLE);
                travel_objects_rich_handsome_select_iv.setVisibility(View.GONE);
                travel_objects_female_temperament_select_iv.setVisibility(View.GONE);
                travel_objects_silly_sweet_white_select_iv.setVisibility(View.GONE);
                travel_objects_phu_bai_select_iv.setVisibility(View.GONE);


                break;
            case 3:


                travel_objects_tyrant_m_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_overbearing_chairman_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_rich_handsome_iv.setBackgroundResource(R.drawable.objects_border_corners);
                travel_objects_female_temperament_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_silly_sweet_white_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_phu_bai_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);

                travel_objects_tyrant_m_select_iv.setVisibility(View.GONE);
                travel_objects_overbearing_chairman_select_iv.setVisibility(View.GONE);
                travel_objects_rich_handsome_select_iv.setVisibility(View.VISIBLE);
                travel_objects_female_temperament_select_iv.setVisibility(View.GONE);
                travel_objects_silly_sweet_white_select_iv.setVisibility(View.GONE);
                travel_objects_phu_bai_select_iv.setVisibility(View.GONE);
                break;
            case 4:


                travel_objects_tyrant_m_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_overbearing_chairman_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_rich_handsome_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_female_temperament_iv.setBackgroundResource(R.drawable.objects_border_corners);
                travel_objects_silly_sweet_white_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_phu_bai_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);

                travel_objects_tyrant_m_select_iv.setVisibility(View.GONE);
                travel_objects_overbearing_chairman_select_iv.setVisibility(View.GONE);
                travel_objects_rich_handsome_select_iv.setVisibility(View.GONE);
                travel_objects_female_temperament_select_iv.setVisibility(View.VISIBLE);
                travel_objects_silly_sweet_white_select_iv.setVisibility(View.GONE);
                travel_objects_phu_bai_select_iv.setVisibility(View.GONE);
                break;
            case 5:

                travel_objects_tyrant_m_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_overbearing_chairman_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_rich_handsome_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_female_temperament_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_silly_sweet_white_iv.setBackgroundResource(R.drawable.objects_border_corners);
                travel_objects_phu_bai_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);

                travel_objects_tyrant_m_select_iv.setVisibility(View.GONE);
                travel_objects_overbearing_chairman_select_iv.setVisibility(View.GONE);
                travel_objects_rich_handsome_select_iv.setVisibility(View.GONE);
                travel_objects_female_temperament_select_iv.setVisibility(View.GONE);
                travel_objects_silly_sweet_white_select_iv.setVisibility(View.VISIBLE);
                travel_objects_phu_bai_select_iv.setVisibility(View.GONE);
                break;
            case 6:

                travel_objects_tyrant_m_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_overbearing_chairman_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_rich_handsome_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_female_temperament_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_silly_sweet_white_iv.setBackgroundResource(R.drawable.objects_border_corners_cancel);
                travel_objects_phu_bai_iv.setBackgroundResource(R.drawable.objects_border_corners);

                travel_objects_tyrant_m_select_iv.setVisibility(View.GONE);
                travel_objects_overbearing_chairman_select_iv.setVisibility(View.GONE);
                travel_objects_rich_handsome_select_iv.setVisibility(View.GONE);
                travel_objects_female_temperament_select_iv.setVisibility(View.GONE);
                travel_objects_silly_sweet_white_select_iv.setVisibility(View.GONE);
                travel_objects_phu_bai_select_iv.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void finish() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("mTravelObjects", mTravelObjects);
        Intent intent = new Intent(TravelObjectsActivity.this, TravelActivity.class);
        intent.putExtras(bundle);
        setResult(YpSettings.TRAVE_OBJECTS, intent);

        super.finish();
    }
}
