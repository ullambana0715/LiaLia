package cn.chono.yopper.activity.appointment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.view.WheelDialog.LocationWheelDialog;
import cn.chono.yopper.view.WheelDialog.OnWheelListener;

/**
 * 邀约地点选择
 * Created by cc on 16/3/23.
 */
public class ReleaseAppointmentAddressActivity extends MainFrameActivity implements View.OnClickListener {


    private TextView appointment_location_tv;

    private EditText appointment_location_et;

    private String locationContext;

    private String strProvince, strCity;


    private Dialog wheelDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.release_appointment_location_activity);

        initView();

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("locationContext")) {
            locationContext = bundle.getString("locationContext");
        }


        if (bundle.containsKey("strProvince")) {
            strProvince = bundle.getString("strProvince");
        }
        if (bundle.containsKey("strCity")) {
            strCity = bundle.getString("strCity");
        }

        if (TextUtils.isEmpty(locationContext)) {
            LocInfo myLoc = Loc.getLoc();
            if (myLoc != null && !CheckUtil.isEmpty(myLoc.getCity())) {

                strProvince=myLoc.getProvince();



            }

        }

        StringBuffer sb = new StringBuffer();

        if (!TextUtils.isEmpty(strProvince))
            sb.append(strProvince);
        if (!TextUtils.isEmpty(strCity))
            sb.append(strCity);

        appointment_location_tv.setText(sb.toString());

        if (!TextUtils.isEmpty(locationContext))
        appointment_location_et.setText(locationContext);


    }


    private void initView() {


        getTvTitle().setText("大概地点");

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvOption = gettvOption();
        tvOption.setVisibility(View.VISIBLE);
        tvOption.setText("完成");

        getOptionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationContext = appointment_location_et.getText().toString().trim();

                if (TextUtils.isEmpty(locationContext)) {

                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentAddressActivity.this, "请输入地点");

                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("locationContext", locationContext);
                bundle.putString("strProvince", strProvince);
                bundle.putString("strCity", strCity);

                Intent intent = new Intent(ReleaseAppointmentAddressActivity.this, ReleaseAppointmentActivity.class);
                intent.putExtras(bundle);

                setResult(YpSettings.MOVEMENT_LOCATION, intent);

                finish();


            }
        });

        tvOption.setTextColor(getResources().getColor(R.color.color_ff7462));


        appointment_location_tv = (TextView) findViewById(R.id.appointment_location_tv);

        appointment_location_et = (EditText) findViewById(R.id.appointment_location_et);

        appointment_location_et.addTextChangedListener(new AddTextWatcher());

        appointment_location_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appointment_location_tv:

                wheelDialog = new LocationWheelDialog(this, strProvince, strCity, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        appointment_location_tv.setText(oneContext);
                        strProvince = oneContext;

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                        strProvince = oneContext;
                        strCity = twoContext;

                        StringBuffer sb = new StringBuffer();
                        sb.append(oneContext);
                        sb.append(" ");
                        sb.append(twoContext);

                        appointment_location_tv.setText(sb);

                    }
                });

                wheelDialog.show();


                break;

        }

    }

    private class AddTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (TextUtils.isEmpty(s)) {
                locationContext = "";
            } else {
                locationContext = s.toString().trim();
            }


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                locationContext = "";
            } else {
                locationContext = s.toString().trim();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
