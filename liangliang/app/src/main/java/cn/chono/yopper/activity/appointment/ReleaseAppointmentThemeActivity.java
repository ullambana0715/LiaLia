package cn.chono.yopper.activity.appointment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.RadioAdapter;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.common.Constant;

/**
 * Created by cc on 16/3/22.
 */
public class ReleaseAppointmentThemeActivity extends MainFrameActivity implements RadioAdapter.OnItemTravelClickLitener, View.OnClickListener {

    private EditText appoint_movement_et;

    private GridView theme_gridView;

    private RadioAdapter adapter;

    private String content;

    private int type;

    private int APPOINT_TYPE;

    private TextView appontment_theme_hint_tv, appointment_theme_hint_tv;

    private ImageView appoint_movement_et_iv;


    private String frompage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.appointment_theme_activity);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.APPOINTMENT_INTENT_YTPE))
            APPOINT_TYPE = bundle.getInt(YpSettings.APPOINTMENT_INTENT_YTPE);

        if (bundle.containsKey("content"))
            content =bundle.getString("content");

        if (bundle.containsKey("type"))
            type = bundle.getInt("type");

        if (bundle.containsKey(YpSettings.FROM_PAGE))
            frompage=bundle.getString(YpSettings.FROM_PAGE);

        initView();
        setMovementData(content);
    }

    private void initView() {


        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView option = gettvOption();
        option.setText(getResources().getString(R.string.affirm));

        getOptionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putSerializable("content", content);
                bundle.putSerializable(YpSettings.APPOINTMENT_INTENT_YTPE, APPOINT_TYPE);
                bundle.putSerializable(YpSettings.FROM_PAGE, frompage);

                Intent intent = new Intent(ReleaseAppointmentThemeActivity.this, ReleaseAppointmentActivity.class);
                intent.putExtras(bundle);

                if (0 != type) {


                    setResult(YpSettings.MOVEMENT_THEME, intent);
                    finish();

                    return;

                }

                ActivityUtil.jump(ReleaseAppointmentThemeActivity.this, ReleaseAppointmentActivity.class, bundle, 0, 100);

                finish();

            }
        });
        appoint_movement_et_iv = (ImageView) findViewById(R.id.appoint_movement_et_iv);
        appointment_theme_hint_tv = (TextView) findViewById(R.id.appointment_theme_hint_tv);
        appontment_theme_hint_tv = (TextView) findViewById(R.id.appontment_theme_hint_tv);

        appoint_movement_et = (EditText) findViewById(R.id.appoint_movement_et);
        appoint_movement_et.addTextChangedListener(new AddTextWatcher());
        theme_gridView = (GridView) findViewById(R.id.theme_gridView);
        appoint_movement_et_iv.setOnClickListener(this);

        adapter = new RadioAdapter(this);
        adapter.setOnItemClickLitener(this);
        theme_gridView.setAdapter(adapter);

    }

    private void setMovementData(String content) {

        Resources res = getResources();

        switch (APPOINT_TYPE) {
            case Constant.APPOINT_TYPE_OTHERS:

                getTvTitle().setText(getResources().getString(R.string.text_movement_theme_select));

                appoint_movement_et.setHint("请选择下列项目或自填");

                String[] movement_thmem = res.getStringArray(R.array.movement_thmem);
                adapter.setData(movement_thmem);

                theme_gridView.setNumColumns(2);

                break;
            case Constant.APPOINT_TYPE_FITNESS:

                getTvTitle().setText(getResources().getString(R.string.text_movement_select));

                appoint_movement_et.setHint("请选择下列项目或自填");

                String[] movement = res.getStringArray(R.array.movement);
                adapter.setData(movement);

                break;
            case Constant.APPOINT_TYPE_DOG:

                getTvTitle().setText(getResources().getString(R.string.text_dog_species_select));

                appoint_movement_et.setHint("请选择下列项目或自填");

                String[] dog_thmem = res.getStringArray(R.array.dog_thmem);
                adapter.setData(dog_thmem);


                break;
            case Constant.APPOINT_TYPE_MARRIED:


                appointment_theme_hint_tv.setVisibility(View.VISIBLE);

                appontment_theme_hint_tv.setVisibility(View.GONE);

                getTvTitle().setText(getResources().getString(R.string.text_occupation));
                appoint_movement_et.setHint("请选择下列职业或自填");
                theme_gridView.setNumColumns(2);

                String[] occupation_thmem = res.getStringArray(R.array.occupation);
                adapter.setData(occupation_thmem);

                break;
        }


        if(!CheckUtil.isEmpty(content)){
            appoint_movement_et.setText(content);
        }

        if (!TextUtils.isEmpty(content)) {
            adapter.setSelectItem(content);
        }

    }

    @Override
    public void onItemClickLitener(int position, String contentStr) {
        this.content = contentStr;
        appoint_movement_et.setText(content);
        appoint_movement_et.requestFocus();  //这是关键
        adapter.setSelectItem(content);
    }

    @Override
    public void onClick(View v) {
        content = "";
        adapter.setSelectItem(content);
        appoint_movement_et.setText(content);
        appoint_movement_et.requestFocus();  //这是关键
    }


    private class AddTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (TextUtils.isEmpty(s)) {
                content = "";
            } else {
                content = s.toString().trim();
            }


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                content = "";
            } else {
                content = s.toString().trim();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (TextUtils.isEmpty(content)) {
                appoint_movement_et_iv.setVisibility(View.GONE);
            } else {
                appoint_movement_et_iv.setVisibility(View.VISIBLE);
            }
            adapter.setSelectItem(content);

        }
    }



}
