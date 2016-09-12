package cn.chono.yopper.activity.find;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.Calendar;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.view.ThreeCityWheelDialog;

/**
 * Created by yangjinyu on 16/3/30.
 */
public class AstrolabeActivity extends MainFrameActivity implements View.OnClickListener, ThreeCityWheelDialog.OnThreeSelectedListener {
    LinearLayout astroableView;

    private Button personalsChatBtn;
    private Button coupeChatBtn;
    private Button fortuneChatBtn;

    TextView mBornDate;
    TextView tBornDate;
    TextView mBornTime;
    TextView tBornTime;
    TextView mMan;
    TextView mWoman;
    TextView tMan;
    TextView tWoman;
    TextView mCheckButton;
    TextView tCheckButton;
    EditText mNameInput;
    EditText tNameInput;
    TextView mBornPlace;
    TextView tBornPlace;
    CheckBox mIsSummer;
    CheckBox tIsSummer;

    TextView taInfoTV;
    LinearLayout taLayout;
    TextView luckInfoTv;
    LinearLayout luckLayout;

    TextView mLuckCheckButton;
    TextView mLuckDate;
    TextView mLuckTime;

    String mName;
    String tName;
    String mSex = "0";
    String tSex = "0";
    String mDate;
    String tDate;
    String luckDate;
    String mSummer = "0";
    String tSummer = "0";
    String mDistNo;
    String tDistNo;
    boolean isTaDistClicked;
    boolean isLuckDateClicked;
    ScrollView mScrollView;
    boolean isTaBornPlaceClicked = false;
    boolean isTaBornDateClicked = false;
    DatePickerDialog mDialog;


    LinearLayout astroable_ll_back;

    TextView astroable_tv_title;


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("星盘解读"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("星盘解读"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astroable);

        PushAgent.getInstance(this).onAppStart();

        astroableView = (LinearLayout) findViewById(R.id.astroable);
        personalsChatBtn = (Button) findViewById(R.id.personals_chat_btn);
        coupeChatBtn = (Button) findViewById(R.id.coupe_chat_btn);
        fortuneChatBtn = (Button) findViewById(R.id.fortune_chat_btn);
        mScrollView = (ScrollView) findViewById(R.id.info);


        astroable_ll_back = (LinearLayout) findViewById(R.id.astroable_ll_back);
        astroable_tv_title = (TextView) findViewById(R.id.astroable_tv_title);

        mBornDate = (TextView) findViewById(R.id.chushengriqi);
        tBornDate = (TextView) findViewById(R.id.tachushengriqi);
        mBornPlace = (TextView) findViewById(R.id.born_input);
        tBornPlace = (TextView) findViewById(R.id.taborn_input);
        mCheckButton = (TextView) findViewById(R.id.chakangeren);
        tCheckButton = (TextView) findViewById(R.id.tachakangeren);
        mLuckCheckButton = (TextView) findViewById(R.id.luck_chakan);
        mLuckDate = (TextView) findViewById(R.id.luckriqi);
        mLuckTime = (TextView) findViewById(R.id.luckshijian);
        mBornTime = (TextView) findViewById(R.id.chushengshijian);
        tBornTime = (TextView) findViewById(R.id.tachushengshijian);
        mMan = (TextView) findViewById(R.id.my_nan);
        mWoman = (TextView) findViewById(R.id.my_nv);
        tMan = (TextView) findViewById(R.id.ta_nan);
        tWoman = (TextView) findViewById(R.id.ta_nv);
        mNameInput = (EditText) findViewById(R.id.name_input);
        tNameInput = (EditText) findViewById(R.id.taname_input);
        mIsSummer = (CheckBox) findViewById(R.id.isSummer);
        tIsSummer = (CheckBox) findViewById(R.id.taisSummer);
        mIsSummer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSummer = isChecked ? "1" : "0";
            }
        });
        tIsSummer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tSummer = isChecked ? "1" : "0";
            }
        });

        taInfoTV = (TextView) findViewById(R.id.ta_info);
        taLayout = (LinearLayout) findViewById(R.id.ta_layout);
        luckInfoTv = (TextView) findViewById(R.id.luck_info);
        luckLayout = (LinearLayout) findViewById(R.id.layout_luck);

        personalsChatBtn.setOnClickListener(this);
        coupeChatBtn.setOnClickListener(this);
        fortuneChatBtn.setOnClickListener(this);
        mLuckTime.setOnClickListener(this);
        mLuckDate.setOnClickListener(this);
        mLuckCheckButton.setOnClickListener(this);
        mCheckButton.setOnClickListener(this);
        tCheckButton.setOnClickListener(this);
        mMan.setOnClickListener(this);
        mWoman.setOnClickListener(this);
        tMan.setOnClickListener(this);
        tWoman.setOnClickListener(this);
        mBornPlace.setOnClickListener(this);
        tBornPlace.setOnClickListener(this);
        mBornDate.setOnClickListener(this);
        tBornDate.setOnClickListener(this);
        mBornTime.setOnClickListener(this);
        tBornTime.setOnClickListener(this);


        astroable_tv_title.setText("星盘解读");

        astroable_ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackEvent();
            }
        });

        //默认显示3个按钮的视图
        if (astroableView.getVisibility() == View.GONE) {
            astroableView.setVisibility(View.VISIBLE);
            AlphaAnimation mShowAnimation = new AlphaAnimation(0, 1);
            mShowAnimation.setDuration(500);
            astroableView.startAnimation(mShowAnimation);

            mScrollView.setVisibility(View.GONE);
            AlphaAnimation mHideAction = new AlphaAnimation(1, 0);
            mHideAction.setDuration(500);
            mScrollView.startAnimation(mHideAction);
            return;
        }
    }

    @Override
    public void onBackPressed() {
        goBackEvent();
    }

    private void goBackEvent() {
        if (astroableView.getVisibility() == View.GONE) {
            astroableView.setVisibility(View.VISIBLE);
            AlphaAnimation mShowAnimation = new AlphaAnimation(0, 1);
            mShowAnimation.setDuration(500);
            astroableView.startAnimation(mShowAnimation);

            mScrollView.setVisibility(View.GONE);
            AlphaAnimation mHideAction = new AlphaAnimation(1, 0);
            mHideAction.setDuration(500);
            mScrollView.startAnimation(mHideAction);
            return;
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.chakangeren) {
            if (TextUtils.isEmpty(mNameInput.getText())) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mDate) || TextUtils.isEmpty(mBornTime.getText())) {
                Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mBornPlace.getText())) {
                Toast.makeText(this, "请选择出生地点", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "chart/index?type=natal"
                    + "&name=" + mNameInput.getText() + "&sex=" + mSex
                    + "&dist=" + mDistNo
                    + "&date=" + mDate + "&time=" + mBornTime.getText()
                    + "&dst=" + mSummer + "&hsys=P");
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, YpSettings.BUNDLE_KEY_WEB_TITLE);
            ActivityUtil.jump(this, TarotWebActivity.class, bundle, 0, 100);
        } else if (id == R.id.tachakangeren) {
            if (TextUtils.isEmpty(mNameInput.getText()) || TextUtils.isEmpty(tNameInput.getText())) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mDate) || TextUtils.isEmpty(tDate) ||
                    TextUtils.isEmpty(mBornTime.getText()) || TextUtils.isEmpty(tBornTime.getText())) {
                Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mBornPlace.getText()) || TextUtils.isEmpty(mBornPlace.getText())) {
                Toast.makeText(this, "请选择出生地点", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "chart/index?type=comparision"
                    + "&name=" + mNameInput.getText() + "&sex=" + mSex
                    + "&dist=" + mDistNo
                    + "&date=" + mDate + "&time=" + mBornTime.getText()
                    + "&dst=" + mSummer
                    + "&name2=" + tNameInput.getText() + "&sex2=" + tSex
                    + "&dist2=" + tDistNo + "&date2=" + tDate
                    + "&time2=" + tBornTime.getText()
                    + "&dst=" + tSummer
                    + "&hsys=P");
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, YpSettings.BUNDLE_KEY_WEB_TITLE);
            ActivityUtil.jump(this, TarotWebActivity.class, bundle, 0, 100);
        } else if (id == R.id.luck_chakan) {
            if (TextUtils.isEmpty(mNameInput.getText())) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mDate) || TextUtils.isEmpty(mBornTime.getText())) {
                Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mBornPlace.getText())) {
                Toast.makeText(this, "请选择出生地点", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(luckDate) || TextUtils.isEmpty(mLuckTime.getText())) {
                Toast.makeText(this, "请选择推运时间", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "chart/index?type=progressed"
                    + "&name=" + mNameInput.getText() + "&sex=" + mSex
                    + "&dist=" + mDistNo
                    + "&date=" + mDate + "&time=" + mBornTime.getText()
                    + "&dst=" + mSummer + "&date2=" + luckDate + "&time2=" + mLuckTime.getText() + "&hsys=P");
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, YpSettings.BUNDLE_KEY_WEB_TITLE);
            ActivityUtil.jump(this, TarotWebActivity.class, bundle, 0, 100);
        } else if (id == R.id.luckriqi) {
            isLuckDateClicked = true;
            Calendar cal = Calendar.getInstance();
            mDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    System.out.println("year:" + year + "monthOfYear:" + monthOfYear + "dayOfMonth:" + dayOfMonth);
                    if ((monthOfYear + 1) < 10) {
                        luckDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    } else {
                        luckDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                    mLuckDate.setText(luckDate);
                }
            }, cal.get(Calendar.YEAR) - 10, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            mDialog.show();
        } else if (id == R.id.taisSummer) {

        } else if (id == R.id.my_nan) {
            mMan.setBackgroundResource(R.drawable.corners_6d5e75_default);
            mWoman.setBackgroundResource(R.drawable.corners_006d5e75_default);
            mSex = "0";
        } else if (id == R.id.my_nv) {
            mMan.setBackgroundResource(R.drawable.corners_006d5e75_default);
            mWoman.setBackgroundResource(R.drawable.corners_6d5e75_default);
            mSex = "1";
        } else if (id == R.id.ta_nan) {
            tMan.setBackgroundResource(R.drawable.corners_6d5e75_default);
            tWoman.setBackgroundResource(R.drawable.corners_006d5e75_default);
            tSex = "0";
        } else if (id == R.id.ta_nv) {
            tMan.setBackgroundResource(R.drawable.corners_006d5e75_default);
            tWoman.setBackgroundResource(R.drawable.corners_6d5e75_default);
            tSex = "1";
        } else if (id == R.id.chushengriqi) {
            Calendar cal = Calendar.getInstance();
            mDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    System.out.println("year:" + year + "monthOfYear:" + monthOfYear + "dayOfMonth:" + dayOfMonth);
                    if ((monthOfYear + 1) < 10) {
                        mDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    } else {
                        mDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                    mBornDate.setText(mDate);
                }
            }, cal.get(Calendar.YEAR) - 10, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            mDialog.show();
            isTaBornDateClicked = false;
            isLuckDateClicked = false;
        } else if (id == R.id.tachushengriqi) {
            Calendar cal = Calendar.getInstance();
            mDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    System.out.println("year:" + year + "monthOfYear:" + monthOfYear + "dayOfMonth:" + dayOfMonth);
                    if ((monthOfYear + 1) < 10) {
                        tDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    } else {
                        tDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                    tBornDate.setText(tDate);
                }
            }, cal.get(Calendar.YEAR) - 10, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            mDialog.show();
            isTaBornDateClicked = true;
            isLuckDateClicked = false;
        } else if (id == R.id.chushengshijian) {
            Calendar cal1 = Calendar.getInstance();
            TimePickerDialog pickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (hourOfDay < 10) {
                        if (minute < 10) {
                            mBornTime.setText("0" + hourOfDay + ":0" + minute);
                        } else {
                            mBornTime.setText(hourOfDay + ":" + minute);
                        }
                    } else {
                        if (minute < 10) {
                            mBornTime.setText("0" + hourOfDay + ":0" + minute);
                        } else {
                            mBornTime.setText(hourOfDay + ":" + minute);
                        }
                    }
                }
            }, cal1.get(Calendar.HOUR), cal1.get(Calendar.MINUTE), true);
            pickerDialog.show();
        } else if (id == R.id.tachushengshijian) {
            Calendar cal = Calendar.getInstance();
            TimePickerDialog pickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (hourOfDay < 10) {
                        if (minute < 10) {
                            tBornTime.setText("0" + hourOfDay + ":0" + minute);
                        } else {
                            tBornTime.setText(hourOfDay + ":" + minute);
                        }
                    } else {
                        if (minute < 10) {
                            tBornTime.setText("0" + hourOfDay + ":0" + minute);
                        } else {
                            tBornTime.setText(hourOfDay + ":" + minute);
                        }
                    }
                }
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true);
            pickerDialog.show();
        } else if (id == R.id.luckshijian) {
            Calendar cal = Calendar.getInstance();
            TimePickerDialog pickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (hourOfDay < 10) {
                        if (minute < 10) {
                            mLuckTime.setText("0" + hourOfDay + ":0" + minute);
                        } else {
                            mLuckTime.setText(hourOfDay + ":" + minute);
                        }
                    } else {
                        if (minute < 10) {
                            mLuckTime.setText("0" + hourOfDay + ":0" + minute);
                        } else {
                            mLuckTime.setText(hourOfDay + ":" + minute);
                        }
                    }
                }
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true);
            pickerDialog.show();
        } else if (id == R.id.taborn_input) {
            isTaBornPlaceClicked = true;
            ThreeCityWheelDialog dialog = new ThreeCityWheelDialog(this, this);
            dialog.show();
        } else if (id == R.id.born_input) {
            isTaBornPlaceClicked = false;
            ThreeCityWheelDialog dialog = new ThreeCityWheelDialog(this, this);
            dialog.show();
        } else if (id == R.id.personals_chat_btn) {
            astroableView.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            taInfoTV.setVisibility(View.GONE);
            taLayout.setVisibility(View.GONE);
            luckInfoTv.setVisibility(View.GONE);
            luckLayout.setVisibility(View.GONE);
            mLuckCheckButton.setVisibility(View.GONE);
            mCheckButton.setVisibility(View.VISIBLE);
            tCheckButton.setVisibility(View.GONE);
        } else if (id == R.id.coupe_chat_btn) {
            mScrollView.setVisibility(View.VISIBLE);
            astroableView.setVisibility(View.GONE);
            taInfoTV.setVisibility(View.VISIBLE);
            taLayout.setVisibility(View.VISIBLE);
            luckInfoTv.setVisibility(View.GONE);
            luckLayout.setVisibility(View.GONE);
            mLuckCheckButton.setVisibility(View.GONE);
            mCheckButton.setVisibility(View.GONE);
            tCheckButton.setVisibility(View.VISIBLE);
        } else if (id == R.id.fortune_chat_btn) {
            mScrollView.setVisibility(View.VISIBLE);
            astroableView.setVisibility(View.GONE);
            taInfoTV.setVisibility(View.GONE);
            taLayout.setVisibility(View.GONE);
            luckInfoTv.setVisibility(View.VISIBLE);
            luckLayout.setVisibility(View.VISIBLE);
            mLuckCheckButton.setVisibility(View.VISIBLE);
            mCheckButton.setVisibility(View.GONE);
            tCheckButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnThreeSelectedListener(String province, String cityStr, String zareaStr, String cityCode) {
        System.out.println("province:" + province + "cityStr:" + cityStr + "zareaStr:" + zareaStr + "cityCode:" + cityCode);
        if (isTaBornPlaceClicked) {
            tBornPlace.setText(province + " " + cityStr + " " + zareaStr);
            tDistNo = cityCode;
        } else {
            mBornPlace.setText(province + " " + cityStr + " " + zareaStr);
            mDistNo = cityCode;
        }
    }

}
