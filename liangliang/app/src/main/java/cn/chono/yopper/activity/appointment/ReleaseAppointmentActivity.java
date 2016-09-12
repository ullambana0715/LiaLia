package cn.chono.yopper.activity.appointment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsBean;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsData;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsRespBean;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsService;
import cn.chono.yopper.Service.Http.DatingPublish.Dine;
import cn.chono.yopper.Service.Http.DatingPublish.Movie;
import cn.chono.yopper.Service.Http.DatingPublish.Other;
import cn.chono.yopper.Service.Http.DatingPublish.Singing;
import cn.chono.yopper.Service.Http.DatingPublish.Sports;
import cn.chono.yopper.Service.Http.DatingPublish.WalkTheDog;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageRespBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageService;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AppointMovieWebData;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.event.DatingsRefreshEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackAppointCall;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.InfoTransformUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.ViewsUtils;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 发布邀约
 * Created by cc on 16/3/23.
 */
public class ReleaseAppointmentActivity extends MainFrameActivity implements View.OnClickListener {

    private LinearLayout appointment_dog_layout, appointment_theme_layout, appointment_movie_layout, appointment_pay_layout;

    private TextView appointment_dog_tv, appointment_tiem, appointment_location_tv, appointment_theme_tv, appointment_movie_tv;


    private RadioGroup appointment_sex_radio_group, appointment_pay_radio_group;

    private RadioButton appointment_sex_man_radiobutton, appointment_sex_women_radiobutton, appointment_sex_nolimit_radiobutton, appointment_pay_aa_radiobutton, appointment_pay_own_radiobutton, appointment_pay_you_radiobutton, appointment_carry_ratiobutton, appointment_companionCondition_ratiobutton;

    private EditText appointment_write_et;

    private TextView appointment_write_et_number_tv;

    private ImageView appointment_write_image_iv, appointment_write_image_fillet_delete_iv;

    private Dialog photoDialog;




    private int sexRequired, costType;

    private boolean carryRequired, companionConditionRequired = true;

    private String explanationContext;

    private Dialog tiem_dialog, canNotPublishDatingDialog, canMsgNotPublishDatingDialog;

    private int APPOINT_TYPE;

    private TextView appointmentTitle;

    private int userId, user_sex;

    private String dogContext;
    private String appointContext;
    private String locationContext, strProvince, strCity;
    private String moviewebData;


    private int days;

    private int month;

    private int Date;

    private String filePath;

    private String meetingTimeType;


    private Dialog loadingDiaog;

    private boolean isRepeat = false;


    //吃饭


    private String meetingTime;
    private int meetingTimeTypes;
    private Integer carry = null;//接送
    private Integer companionCondition = null;

    //看电影

    private double longtitude = 121.478844;
    private double latitude = 31.240517;

    private AppointMovieWebData appointMovieWebData;


    private String frompage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.release_appointment_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(YpSettings.APPOINTMENT_INTENT_YTPE))
            APPOINT_TYPE = bundle.getInt(YpSettings.APPOINTMENT_INTENT_YTPE);

        if (bundle.containsKey("content")) {
            appointContext = bundle.getString("content");

        }

        if (bundle.containsKey(YpSettings.FROM_PAGE))
            frompage = bundle.getString(YpSettings.FROM_PAGE);

        initView();
        initLoadingDialog();
        setListener();//这个方法必须在setAppointmentTitle()前

        setAppointmentTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Loc.sendLocControlMessage(true);



    }


    @Override
    protected void onPause() {
        super.onPause();
        Loc.sendLocControlMessage(false);
    }


    private void initView() {


        appointmentTitle = getTvTitle();

        getBtnGoBack().setVisibility(View.GONE);

        TextView tvBack = gettvBack();
        tvBack.setVisibility(View.VISIBLE);
        tvBack.setText("取消");

        TextView tvOption = gettvOption();
        tvOption.setVisibility(View.VISIBLE);
        tvOption.setText("发布");
        tvOption.setTextColor(getResources().getColor(R.color.color_ff7462));

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getOptionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dataDealWith();


            }
        });


        appointment_dog_layout = (LinearLayout) findViewById(R.id.appointment_dog_layout);

        appointment_theme_layout = (LinearLayout) findViewById(R.id.appointment_theme_layout);

        appointment_movie_layout = (LinearLayout) findViewById(R.id.appointment_movie_layout);

        appointment_pay_layout = (LinearLayout) findViewById(R.id.appointment_pay_layout);

        appointment_dog_tv = (TextView) findViewById(R.id.appointment_dog_tv);

        appointment_theme_tv = (TextView) findViewById(R.id.appointment_theme_tv);

        appointment_location_tv = (TextView) findViewById(R.id.appointment_location_tv);

        appointment_movie_tv = (TextView) findViewById(R.id.appointment_movie_tv);

        appointment_tiem = (TextView) findViewById(R.id.appointment_tiem);

        appointment_sex_radio_group = (RadioGroup) findViewById(R.id.appointment_sex_radio_group);

        appointment_pay_radio_group = (RadioGroup) findViewById(R.id.appointment_pay_radio_group);

        appointment_sex_man_radiobutton = (RadioButton) findViewById(R.id.appointment_sex_man_radiobutton);

        appointment_sex_women_radiobutton = (RadioButton) findViewById(R.id.appointment_sex_women_radiobutton);

        appointment_sex_nolimit_radiobutton = (RadioButton) findViewById(R.id.appointment_sex_nolimit_radiobutton);

        appointment_pay_aa_radiobutton = (RadioButton) findViewById(R.id.appointment_pay_aa_radiobutton);

        appointment_pay_own_radiobutton = (RadioButton) findViewById(R.id.appointment_pay_own_radiobutton);

        appointment_pay_you_radiobutton = (RadioButton) findViewById(R.id.appointment_pay_you_radiobutton);

        appointment_carry_ratiobutton = (RadioButton) findViewById(R.id.appointment_carry_ratiobutton);

        appointment_companionCondition_ratiobutton = (RadioButton) findViewById(R.id.appointment_companionCondition_ratiobutton);

        appointment_write_et = (EditText) findViewById(R.id.appointment_write_et);

        appointment_write_et_number_tv = (TextView) findViewById(R.id.appointment_write_et_number_tv);

        appointment_write_image_iv = (ImageView) findViewById(R.id.appointment_write_image_iv);

        appointment_write_image_fillet_delete_iv = (ImageView) findViewById(R.id.appointment_write_image_fillet_delete_iv);


    }

    /**
     * 设置头像是对dialog的初始化
     */
    private void initLoadingDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout loadingview = (LinearLayout) inflater.inflate(R.layout.loading_dialog, null);

        loadingDiaog = DialogUtil.hineDialog(ReleaseAppointmentActivity.this, loadingview);
    }


    private void setAppointmentTitle() {

        appointment_companionCondition_ratiobutton.setVisibility(View.VISIBLE);
        switch (APPOINT_TYPE) {
            case Constant.APPOINT_TYPE_EAT://吃饭

                appointmentTitle.setText(getResources().getString(R.string.text_EAT));


                appointment_dog_layout.setVisibility(View.GONE);//隐藏溜狗

                appointment_theme_layout.setVisibility(View.GONE);//隐藏主题

                appointment_movie_layout.setVisibility(View.GONE);//隐藏影片

                break;
            case Constant.APPOINT_TYPE_MOVIE://电影
                appointmentTitle.setText(getResources().getString(R.string.text_MOVIE));

                appointment_dog_layout.setVisibility(View.GONE);//隐藏溜狗

                appointment_theme_layout.setVisibility(View.GONE);//隐藏主题

                appointment_companionCondition_ratiobutton.setVisibility(View.GONE);
                break;
            case Constant.APPOINT_TYPE_FITNESS://运动
                appointmentTitle.setText(getResources().getString(R.string.text_FITNESS));

                appointment_dog_layout.setVisibility(View.GONE);//隐藏溜狗

                appointment_movie_layout.setVisibility(View.GONE);//隐藏影片


                if (!TextUtils.isEmpty(appointContext.trim()))
                    appointment_theme_tv.setText(appointContext);


                break;
            case Constant.APPOINT_TYPE_DOG://溜狗
                appointmentTitle.setText(getResources().getString(R.string.text_DOG));


                appointment_theme_layout.setVisibility(View.GONE);//隐藏主题

                appointment_movie_layout.setVisibility(View.GONE);//隐藏影片
                break;
            case Constant.APPOINT_TYPE_KTV://K歌
                appointmentTitle.setText(getResources().getString(R.string.text_KTV));

                appointment_dog_layout.setVisibility(View.GONE);//隐藏溜狗

                appointment_movie_layout.setVisibility(View.GONE);//隐藏影片

                appointment_theme_layout.setVisibility(View.GONE);//隐藏主题


                if (1 == user_sex) {
                    appointment_companionCondition_ratiobutton.setText(Constant.Companion_Type_Carry_Two_Or_Three_Friends_Male_Str);
                } else {
                    appointment_companionCondition_ratiobutton.setText(Constant.Companion_Type_Carry_Two_Or_Three_Friends_Female_Str);
                }


                break;
            case Constant.APPOINT_TYPE_OTHERS://其他
                appointmentTitle.setText(getResources().getString(R.string.text_other));

                appointment_dog_layout.setVisibility(View.GONE);//隐藏溜狗

                appointment_movie_layout.setVisibility(View.GONE);//隐藏影片

                appointment_pay_layout.setVisibility(View.GONE);//隐藏买单

                break;
        }
    }

    private void setListener() {


        userId = LoginUser.getInstance().getUserId();
        user_sex = DbHelperUtils.getDbUserSex(userId);


        switch (user_sex) {
            case 1://男
                appointment_sex_women_radiobutton.setChecked(true);
                sexRequired = Constant.TargetSex_Type_Female;

                appointment_pay_you_radiobutton.setVisibility(View.GONE);
                appointment_pay_own_radiobutton.setVisibility(View.VISIBLE);//我买单
                appointment_pay_own_radiobutton.setChecked(true);//默认我买单
                costType = Constant.CostType_I;


                appointment_carry_ratiobutton.setText(Constant.Carry_Type_I_Pick_Str);
                appointment_companionCondition_ratiobutton.setText(Constant.Companion_Type_Carry_A_Girlfriends_Male_Str);

                break;
            case 2://女
                appointment_sex_man_radiobutton.setChecked(true);
                sexRequired = Constant.TargetSex_Type_Male;

                appointment_pay_own_radiobutton.setVisibility(View.GONE);//我买单
                appointment_pay_you_radiobutton.setVisibility(View.VISIBLE);//你买单
                appointment_pay_you_radiobutton.setChecked(true);//默认你买单
                costType = Constant.CostType_You;

                appointment_carry_ratiobutton.setText(Constant.Carry_Type_Need_I_Pick_Str);

                appointment_companionCondition_ratiobutton.setText(Constant.Companion_Type_Carry_A_Girlfriends_Female_Str);

                break;


        }

        appointment_sex_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.appointment_sex_man_radiobutton://1男
                        sexRequired = Constant.TargetSex_Type_Male;
                        break;
                    case R.id.appointment_sex_women_radiobutton://2女
                        sexRequired = Constant.TargetSex_Type_Female;
                        break;
                    case R.id.appointment_sex_nolimit_radiobutton://0无限
                        sexRequired = Constant.TargetSex_Type_Any;
                        break;
                }

            }
        });


        appointment_pay_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.appointment_pay_own_radiobutton://0 我买单
                        costType = Constant.CostType_I;
                        break;
                    case R.id.appointment_pay_aa_radiobutton://1 AA
                        costType = Constant.CostType_AA;
                        break;
                    case R.id.appointment_pay_you_radiobutton:// 2 你买单
                        costType = Constant.CostType_You;
                        break;
                }


            }
        });

        appointment_carry_ratiobutton.setChecked(false);
        appointment_carry_ratiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (carryRequired) {
                    carryRequired = false;
                } else {
                    carryRequired = true;
                }
                appointment_carry_ratiobutton.setChecked(carryRequired);

            }
        });

        appointment_companionCondition_ratiobutton.setChecked(true);
        appointment_companionCondition_ratiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (companionConditionRequired) {
                    companionConditionRequired = false;
                } else {
                    companionConditionRequired = true;
                }
                appointment_companionCondition_ratiobutton.setChecked(companionConditionRequired);

            }
        });

        appointment_write_et.addTextChangedListener(new AddTextWatcher());


        appointment_theme_tv.setOnClickListener(this);

        appointment_write_image_iv.setOnClickListener(this);

        appointment_write_image_fillet_delete_iv.setOnClickListener(this);

        appointment_location_tv.setOnClickListener(this);

        appointment_dog_tv.setOnClickListener(this);

        appointment_movie_tv.setOnClickListener(this);

        appointment_tiem.setOnClickListener(this);


    }


    class AddTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (!TextUtils.isEmpty(s)) {
                explanationContext = s.toString().trim();

            }


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                explanationContext = s.toString().trim();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(explanationContext)) {

                int length = explanationContext.length();

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(length);

                stringBuilder.append("/200");


                appointment_write_et_number_tv.setText(stringBuilder.toString());


            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appointment_write_image_fillet_delete_iv:

                appointment_write_image_iv.setImageResource(R.drawable.travel_image);
                filePath = null;
                appointment_write_image_fillet_delete_iv.setVisibility(View.GONE);

                break;
            case R.id.appointment_write_image_iv:

                photoDialog = DialogUtil.createPhotoDialog(this, view -> {


                    photoDialog.dismiss();


                    MultiImageSelector.create()
                            .showCamera(true) // 是否显示相机. 默认为显示
                            .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                            .single() // 单选模式
//             .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                            .start(this, YpSettings.ICON_REQUEST_IMAGE);


                });


                if (!isFinishing()) {
                    photoDialog.show();
                }

                break;
            case R.id.appointment_location_tv://地点

                ViewsUtils.preventViewMultipleClick(v, 1000);

                switch (APPOINT_TYPE) {
                    case Constant.APPOINT_TYPE_EAT://吃饭
                        releaseAppointmentAddress();
                        break;
                    case Constant.APPOINT_TYPE_MOVIE://电影


                        //跳转到web 查看帮助
                        Bundle bd = new Bundle();
                        bd.putString(YpSettings.BUNDLE_KEY_WEB_URL, "app/loader.html");
                        bd.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "电影");
                        bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);
                        bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST, true);

                        bd.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MOVIE);
                        bd.putInt(YpSettings.SOURCE_YTPE_KEY, 100);


                        Intent it = new Intent(ReleaseAppointmentActivity.this, SimpleWebViewActivity.class);
                        it.putExtras(bd);

                        startActivityForResult(it, YpSettings.WEB_CODE);


                        break;
                    case Constant.APPOINT_TYPE_FITNESS://运动

                        releaseAppointmentAddress();

                        break;
                    case Constant.APPOINT_TYPE_DOG://溜狗
                        releaseAppointmentAddress();
                        break;
                    case Constant.APPOINT_TYPE_KTV://K歌

                        releaseAppointmentAddress();

                        break;
                    case Constant.APPOINT_TYPE_OTHERS://其他

                        releaseAppointmentAddress();

                        break;
                }


                break;
            case R.id.appointment_tiem://时间
                ViewsUtils.preventViewMultipleClick(v, 1000);

                tiem_dialog = DialogUtil.createAppointDialog(ReleaseAppointmentActivity.this, "选择时间", "不限时间", "平时周末", "选择时间", new TitmBackTravelCall());

                tiem_dialog.show();

                break;
            case R.id.appointment_theme_tv://主题


                ViewsUtils.preventViewMultipleClick(v, 1000);

                switch (APPOINT_TYPE) {


                    case Constant.APPOINT_TYPE_FITNESS://运动

                        Bundle fitness = new Bundle();
                        fitness.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_FITNESS);
                        fitness.putString("content", appointContext);
                        fitness.putInt("type", 2);

                        Intent fitnessInt = new Intent(ReleaseAppointmentActivity.this, ReleaseAppointmentThemeActivity.class);

                        fitnessInt.putExtras(fitness);

                        startActivityForResult(fitnessInt, YpSettings.MOVEMENT_THEME);


                        break;

                    case Constant.APPOINT_TYPE_OTHERS://其他

                        Bundle limit = new Bundle();
                        limit.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_OTHERS);
                        limit.putString("content", appointContext);
                        limit.putInt("type", 2);

                        Intent limitInt = new Intent(ReleaseAppointmentActivity.this, ReleaseAppointmentThemeActivity.class);

                        limitInt.putExtras(limit);

                        startActivityForResult(limitInt, YpSettings.MOVEMENT_THEME);

                        break;
                }

                break;
            case R.id.appointment_dog_tv://狗狗的类型

                Bundle limit = new Bundle();
                limit.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_DOG);
                limit.putString("content", dogContext);
                limit.putInt("type", 2);

                Intent limitInt = new Intent(ReleaseAppointmentActivity.this, ReleaseAppointmentThemeActivity.class);

                limitInt.putExtras(limit);

                startActivityForResult(limitInt, YpSettings.MOVEMENT_THEME);


                break;
            case R.id.appointment_movie_tv://电影

                //跳转到web 查看帮助
                Bundle bd = new Bundle();
                bd.putString(YpSettings.BUNDLE_KEY_WEB_URL, "app/loader.html");
                bd.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "电影");
                bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);
                bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST, true);

                bd.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MOVIE);
                bd.putInt(YpSettings.SOURCE_YTPE_KEY, 100);


                Intent it = new Intent(ReleaseAppointmentActivity.this, SimpleWebViewActivity.class);
                it.putExtras(bd);

                startActivityForResult(it, YpSettings.WEB_CODE);
                break;
        }
    }

    private void dataDealWith() {

        switch (APPOINT_TYPE) {
            case Constant.APPOINT_TYPE_EAT://吃饭


                if (TextUtils.isEmpty(locationContext)) {//地点

                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));

                } else if (TextUtils.isEmpty(appointment_tiem.getText().toString().trim())) {//时间
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(explanationContext)) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else {

                    meetingTimeTypes = InfoTransformUtils.getMeetingTime(meetingTimeType);

                    if (TextUtils.equals("选择时间", meetingTimeType)) {
                        meetingTime = appointment_tiem.getText().toString().trim();
                    }


                    if (carryRequired) {//接送
                        carry = InfoTransformUtils.getCarry(appointment_carry_ratiobutton.getText().toString().trim());
                    } else {
                        carry = InfoTransformUtils.getCarry(Constant.Carry_Type_No_Str);
                    }


                    if (companionConditionRequired) {
                        companionCondition = InfoTransformUtils.getCompanionCondition(appointment_companionCondition_ratiobutton.getText().toString().trim());

                    } else {
                        companionCondition = InfoTransformUtils.getCompanionCondition("不携带");
                    }

                    if (isRepeat) return;
                    loadingDiaog.show();
                    douploadingUserImg(filePath);


                }


                break;
            case Constant.APPOINT_TYPE_MOVIE://电影


                if (null == appointMovieWebData) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(appointment_tiem.getText().toString().trim())) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(explanationContext)) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else {


                    meetingTimeTypes = InfoTransformUtils.getMeetingTime(meetingTimeType);

                    if (TextUtils.equals("选择时间", meetingTimeType)) {
                        meetingTime = appointment_tiem.getText().toString().trim();
                    }

                    if (carryRequired) {//接送
                        carry = InfoTransformUtils.getCarry(appointment_carry_ratiobutton.getText().toString().trim());
                    } else {
                        carry = InfoTransformUtils.getCarry(Constant.Carry_Type_No_Str);
                    }

                    if (companionConditionRequired) {
                        companionCondition = InfoTransformUtils.getCompanionCondition(appointment_companionCondition_ratiobutton.getText().toString().trim());

                    } else {
                        companionCondition = InfoTransformUtils.getCompanionCondition(Constant.Companion_Type_No_Carry_Str);
                    }
                    if (isRepeat) return;
                    loadingDiaog.show();
                    douploadingUserImg(filePath);

                }

                break;
            case Constant.APPOINT_TYPE_FITNESS://运动


                if (TextUtils.isEmpty(appointContext.trim())) {//主题
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(locationContext)) {//地点
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(appointment_tiem.getText().toString().trim())) {//时间
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(explanationContext)) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else {

                    meetingTimeTypes = InfoTransformUtils.getMeetingTime(meetingTimeType);

                    if (TextUtils.equals("选择时间", meetingTimeType)) {
                        meetingTime = appointment_tiem.getText().toString().trim();
                    }


                    if (carryRequired) {//接送
                        carry = InfoTransformUtils.getCarry(appointment_carry_ratiobutton.getText().toString().trim());
                    } else {
                        carry = InfoTransformUtils.getCarry(Constant.Carry_Type_No_Str);
                    }


                    if (companionConditionRequired) {
                        companionCondition = InfoTransformUtils.getCompanionCondition(appointment_companionCondition_ratiobutton.getText().toString().trim());

                    } else {
                        companionCondition = InfoTransformUtils.getCompanionCondition(Constant.Companion_Type_No_Carry_Str);
                    }
                    if (isRepeat) return;
                    loadingDiaog.show();
                    douploadingUserImg(filePath);

                }


                break;
            case Constant.APPOINT_TYPE_DOG://溜狗

                if (TextUtils.isEmpty(dogContext.trim())) {//主题
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(locationContext)) {//地点
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(appointment_tiem.getText().toString().trim())) {//时间
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(explanationContext)) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else {

                    meetingTimeTypes = InfoTransformUtils.getMeetingTime(meetingTimeType);

                    if (TextUtils.equals("选择时间", meetingTimeType)) {
                        meetingTime = appointment_tiem.getText().toString().trim();
                    }


                    if (carryRequired) {//接送
                        carry = InfoTransformUtils.getCarry(appointment_carry_ratiobutton.getText().toString().trim());
                    } else {
                        carry = InfoTransformUtils.getCarry(Constant.Carry_Type_No_Str);
                    }


                    if (companionConditionRequired) {
                        companionCondition = InfoTransformUtils.getCompanionCondition(appointment_companionCondition_ratiobutton.getText().toString().trim());

                    } else {
                        companionCondition = InfoTransformUtils.getCompanionCondition(Constant.Companion_Type_No_Carry_Str);
                    }
                    if (isRepeat) return;
                    loadingDiaog.show();
                    douploadingUserImg(filePath);

                }


                break;
            case Constant.APPOINT_TYPE_KTV://K歌


                if (TextUtils.isEmpty(locationContext)) {//地点
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(appointment_tiem.getText().toString().trim())) {//时间
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(explanationContext)) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else {

                    meetingTimeTypes = InfoTransformUtils.getMeetingTime(meetingTimeType);

                    if (TextUtils.equals("选择时间", meetingTimeType)) {
                        meetingTime = appointment_tiem.getText().toString().trim();
                    }


                    if (carryRequired) {//接送
                        carry = InfoTransformUtils.getCarry(appointment_carry_ratiobutton.getText().toString().trim());
                    } else {
                        carry = InfoTransformUtils.getCarry(Constant.Carry_Type_No_Str);
                    }


                    if (companionConditionRequired) {
                        companionCondition = InfoTransformUtils.getCompanionCondition(appointment_companionCondition_ratiobutton.getText().toString().trim());

                    } else {
                        companionCondition = InfoTransformUtils.getCompanionCondition(Constant.Companion_Type_No_Carry_Str);
                    }

                    if (isRepeat) return;
                    loadingDiaog.show();
                    douploadingUserImg(filePath);
                }


                break;
            case Constant.APPOINT_TYPE_OTHERS://其他


                if (TextUtils.isEmpty(appointContext.trim())) {//主题
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(locationContext)) {//地点
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(appointment_tiem.getText().toString().trim())) {//时间
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else if (TextUtils.isEmpty(explanationContext)) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, getString(R.string.text_appointment_hini));
                } else {

                    meetingTimeTypes = InfoTransformUtils.getMeetingTime(meetingTimeType);

                    if (TextUtils.equals("选择时间", meetingTimeType)) {
                        meetingTime = appointment_tiem.getText().toString().trim();
                    }


                    if (carryRequired) {//接送
                        carry = InfoTransformUtils.getCarry(appointment_carry_ratiobutton.getText().toString().trim());
                    } else {
                        carry = InfoTransformUtils.getCarry(Constant.Carry_Type_No_Str);
                    }


                    if (companionConditionRequired) {
                        companionCondition = InfoTransformUtils.getCompanionCondition(appointment_companionCondition_ratiobutton.getText().toString().trim());

                    } else {
                        companionCondition = InfoTransformUtils.getCompanionCondition(Constant.Companion_Type_No_Carry_Str);
                    }
                    if (isRepeat) return;
                    loadingDiaog.show();
                    douploadingUserImg(filePath);

                }


                break;
        }


    }


    /**
     * 选择时间弹出窗回调
     */
    private class TitmBackTravelCall implements BackAppointCall {

        @Override
        public void onOneLayout(View view, Object... obj) {
            tiem_dialog.dismiss();
            TextView tv = (TextView) view;
            appointment_tiem.setText(tv.getText());
            meetingTimeType = tv.getText().toString().trim();
        }

        @Override
        public void onTwoLayout(View view, Object... obj) {
            tiem_dialog.dismiss();
            TextView tv = (TextView) view;
            appointment_tiem.setText(tv.getText());
            meetingTimeType = tv.getText().toString().trim();
        }

        @Override
        public void onThreeLayout(View view, Object... obj) {


            tiem_dialog.dismiss();


            Intent timeIt = new Intent(ReleaseAppointmentActivity.this, SpecificTimeActivity.class);
            Bundle timeItBd = new Bundle();
            timeItBd.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, APPOINT_TYPE);

            timeItBd.putInt("days", days);
            timeItBd.putInt("month", month);
            timeItBd.putInt("Date", Date);

            timeIt.putExtras(timeItBd);
            startActivityForResult(timeIt, YpSettings.MOVEMENT_TIME);

        }


    }

    private void releaseAppointmentAddress() {

        Bundle bundle = new Bundle();

        bundle.putString("locationContext", locationContext);
        bundle.putString("strProvince", strProvince);
        bundle.putString("strCity", strCity);


        Intent limitIntother = new Intent(ReleaseAppointmentActivity.this, ReleaseAppointmentAddressActivity.class);
        limitIntother.putExtras(bundle);
        startActivityForResult(limitIntother, YpSettings.MOVEMENT_LOCATION);

    }




    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {


            case YpSettings.MOVEMENT_TIME:

                if (requestCode == YpSettings.MOVEMENT_TIME) {


                    if (data != null) {


                        Bundle bundle = data.getExtras();

                        if (bundle.containsKey("days"))
                            days = bundle.getInt("days");
                        if (bundle.containsKey("month"))
                            month = bundle.getInt("month");
                        if (bundle.containsKey("Date"))
                            Date = bundle.getInt("Date");


                        if (0 != days && 0 != month && 0 != Date) {
                            StringBuffer sb = new StringBuffer();
                            sb.append(days);
                            sb.append("-");
                            sb.append(month);
                            sb.append("-");
                            sb.append(Date);

                            appointment_tiem.setText(sb.toString());


                            meetingTimeType = "选择时间";

                        }


                    }


                }
                break;


            case YpSettings.WEB_CODE:
                if (resultCode == YpSettings.WEB_CODE) {
                    if (null != data) {
                        moviewebData = data.getExtras().getString(YpSettings.WEB_DATA_KEY);
                        appointMovieWebData = JsonUtils.fromJson(moviewebData, AppointMovieWebData.class);
                        if (null != appointMovieWebData) {
                            if (!TextUtils.isEmpty(appointMovieWebData.getName())) {
                                appointment_movie_tv.setText(appointMovieWebData.getName());
                            }
                            if (!TextUtils.isEmpty(appointMovieWebData.getCinema())) {
                                appointment_location_tv.setText(appointMovieWebData.getCinema());

                            }
                        }


                        LogUtils.e("--webData---=" + moviewebData);
                    } else {
                        LogUtils.e("--webData=");
                    }

                }

                break;


            case YpSettings.MOVEMENT_LOCATION:
                if (resultCode == YpSettings.MOVEMENT_LOCATION) {

                    if (data != null) {
                        locationContext = data.getExtras().getString("locationContext");
                        strProvince = data.getExtras().getString("strProvince");
                        strCity = data.getExtras().getString("strCity");


                        if (!TextUtils.isEmpty(locationContext)) {
                            appointment_location_tv.setText(locationContext);
                        }

                    }

                }


                break;
            case YpSettings.MOVEMENT_THEME:
                if (resultCode == YpSettings.MOVEMENT_THEME) {

                    if (data != null) {

                        if (APPOINT_TYPE == Constant.APPOINT_TYPE_DOG) {
                            dogContext = (String) data.getExtras().getSerializable("content");
                            if (!TextUtils.isEmpty(dogContext)) {
                                appointment_dog_tv.setText(dogContext);
                            } else {
                                appointment_dog_tv.setText("");
                            }
                            return;

                        }

                        appointContext = (String) data.getExtras().getSerializable("content");

                        if (!TextUtils.isEmpty(appointContext.trim())) {
                            appointment_theme_tv.setText(appointContext);
                        } else {
                            appointment_theme_tv.setText("");
                        }

                    }

                }


                break;


            case YpSettings.ICON_REQUEST_IMAGE:// 拍照


                if (resultCode == RESULT_OK) {


                    List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);


                    for (String p : mSelectPath) {

                        setUriBitmap(p);

                    }


                }


                break;


            default:
                break;

        }

    }


    /**
     * @throws
     * @Title: setUriBitmap
     * @Description: 上传图片前对图片的操作(这里用一句话描述这个方法的作用)
     * @param: @param url
     * @return: void
     */
    private void setUriBitmap(String file_Path) {

        appointment_write_image_fillet_delete_iv.setVisibility(View.VISIBLE);

        Glide.with(this).load(file_Path).into(appointment_write_image_iv);

        Bitmap bm = ImgUtils.resizesBitmap(file_Path);

        if (null != bm) {
            // 保存在自己定义文件的路径

            filePath = ImgUtils.saveImgFile(this, bm);
            bm.recycle();
            if (!CheckUtil.isEmpty(filePath)) {

            } else {
                DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, "选取失败，请重新选择上传！");
            }
        } else {
            DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, "选取失败，请重新选择上传！");
        }

    }

    /**
     * 上传图片
     *
     * @param filePath
     */
    private void douploadingUserImg(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            postData("");

            return;
        }


        UploadingDatingsImageBean datingsImageBean = new UploadingDatingsImageBean();

        datingsImageBean.setFilePath(filePath);

        UploadingDatingsImageService datingsImageService = new UploadingDatingsImageService(this);

        datingsImageService.parameter(datingsImageBean);

        datingsImageService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UploadingDatingsImageRespBean datingsImageRespBean = (UploadingDatingsImageRespBean) respBean;

                String url = datingsImageRespBean.getResp();

                postData(url);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                postData("");

            }
        });
        datingsImageService.enqueue();


    }


    private void postData(String filePath) {


        String firstArea = "", secondArea = "";

        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            latitude = myLoc.getLoc().getLatitude();
            longtitude = myLoc.getLoc().getLongitude();
            firstArea = myLoc.getProvince();
            secondArea = myLoc.getCity();


        }
        LatLng pt = new LatLng(latitude, longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);


        DatingsBean datingsBean = new DatingsBean();

        datingsBean.setLat(pt.latitude);

        datingsBean.setLng(pt.longitude);

        datingsBean.setFirstArea(firstArea);
        datingsBean.setSecondArea(secondArea);


        switch (APPOINT_TYPE) {
            case Constant.APPOINT_TYPE_EAT://吃饭


                datingsBean.setActivityType(Constant.APPOINT_TYPE_EAT);

                Dine dine = new Dine();

                dine.setCostType(costType);

                if (null != companionCondition) {
                    dine.setCompanionCondition(companionCondition);
                }

                dine.setFirstArea(strProvince);

                dine.setSecondArea(strCity);

                dine.setAddress(locationContext);

                dine.setMeetingTime(meetingTime);

                dine.setMeetingTimeType(meetingTimeTypes);

                dine.setTargetSex(sexRequired);


                if (carry != null) {
                    dine.setCarry(carry);
                }

                dine.setDescription(explanationContext);

                dine.setPhotoUrl(filePath);

                datingsBean.setDine(dine);

                break;
            case Constant.APPOINT_TYPE_MOVIE://电影

                datingsBean.setActivityType(Constant.APPOINT_TYPE_MOVIE);


                Movie movie = new Movie();

                movie.setName(appointMovieWebData.getName());

                movie.setCostType(costType);

                movie.setFirstArea(firstArea);

                movie.setSecondArea(secondArea);

                movie.setAddress(appointMovieWebData.getAddress());

                movie.setMeetingTime(meetingTime);

                movie.setMeetingTimeType(meetingTimeTypes);

                movie.setTargetSex(sexRequired);

                if (carry != null) {
                    movie.setCarry(carry);
                }

                movie.setDescription(explanationContext);

                movie.setPhotoUrl(filePath);

                datingsBean.setMovie(movie);

                break;
            case Constant.APPOINT_TYPE_FITNESS://运动

                datingsBean.setActivityType(Constant.APPOINT_TYPE_FITNESS);

                Sports sports = new Sports();

                sports.setTheme(appointContext);


                sports.setCostType(costType);

                if (null != companionCondition) {
                    sports.setCompanionCondition(companionCondition);
                }

                sports.setFirstArea(strProvince);

                sports.setSecondArea(strCity);

                sports.setAddress(locationContext);

                sports.setMeetingTime(meetingTime);

                sports.setMeetingTimeType(meetingTimeTypes);

                sports.setTargetSex(sexRequired);


                if (carry != null) {
                    sports.setCarry(carry);
                }

                sports.setDescription(explanationContext);

                sports.setPhotoUrl(filePath);

                datingsBean.setSports(sports);


                break;
            case Constant.APPOINT_TYPE_DOG://溜狗


                datingsBean.setActivityType(Constant.APPOINT_TYPE_DOG);

                WalkTheDog walkTheDog = new WalkTheDog();

                walkTheDog.setDogType(dogContext);


                if (null != companionCondition) {
                    walkTheDog.setCompanionCondition(companionCondition);
                }

                walkTheDog.setFirstArea(strProvince);

                walkTheDog.setSecondArea(strCity);

                walkTheDog.setAddress(locationContext);

                walkTheDog.setMeetingTime(meetingTime);

                walkTheDog.setMeetingTimeType(meetingTimeTypes);

                walkTheDog.setTargetSex(sexRequired);


                if (carry != null) {
                    walkTheDog.setCarry(carry);
                }

                walkTheDog.setDescription(explanationContext);

                walkTheDog.setPhotoUrl(filePath);

                datingsBean.setWalkTheDog(walkTheDog);

                break;
            case Constant.APPOINT_TYPE_KTV://K歌

                datingsBean.setActivityType(Constant.APPOINT_TYPE_KTV);

                Singing singing = new Singing();

                singing.setCostType(costType);

                if (null != companionCondition) {
                    singing.setCompanionCondition(companionCondition);
                }

                singing.setFirstArea(strProvince);

                singing.setSecondArea(strCity);

                singing.setAddress(locationContext);

                singing.setMeetingTime(meetingTime);

                singing.setMeetingTimeType(meetingTimeTypes);

                singing.setTargetSex(sexRequired);


                if (carry != null) {
                    singing.setCarry(carry);
                }

                singing.setDescription(explanationContext);

                singing.setPhotoUrl(filePath);


                datingsBean.setSinging(singing);


                break;
            case Constant.APPOINT_TYPE_OTHERS://其他

                datingsBean.setActivityType(Constant.APPOINT_TYPE_OTHERS);

                Other other = new Other();

                other.setTheme(appointContext);


                if (null != companionCondition) {
                    other.setCompanionCondition(companionCondition);
                }

                other.setFirstArea(strProvince);

                other.setSecondArea(strCity);

                other.setAddress(locationContext);

                other.setMeetingTime(meetingTime);

                other.setMeetingTimeType(meetingTimeTypes);

                other.setTargetSex(sexRequired);


                if (carry != null) {
                    other.setCarry(carry);
                }

                other.setDescription(explanationContext);

                other.setPhotoUrl(filePath);

                datingsBean.setOther(other);


                break;
        }


        DatingsService datingsService = new DatingsService(this);

        datingsService.parameter(datingsBean);

        datingsService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                isRepeat = false;


                DatingsRespBean datingsRespBean = (DatingsRespBean) respBean;

                DatingsData resp = datingsRespBean.getResp();

                if (null == resp) {

                    return;
                }

                if (resp.isSuccess()) {


                    getUserInfo(LoginUser.getInstance().getUserId());

                    if (TextUtils.equals("UserAppointListActivity", frompage)) {

                        RxBus.get().post("DatingsRefreshEvent",new DatingsRefreshEvent(2));

                    } else if (TextUtils.equals("AppointmentFragment", frompage)) {

                        RxBus.get().post("DatingsRefreshEvent",new DatingsRefreshEvent(1));
                    }

                    finish();

                    return;
                }


                String limitMsg = resp.getLimitMsg();

                if (!TextUtils.isEmpty(limitMsg)) {


                    canMsgNotPublishDatingDialog = DialogUtil.createHintOperateDialog(ReleaseAppointmentActivity.this, "", limitMsg, "", "确定", new BackCallListener() {

                        @Override
                        public void onEnsure(View view, Object... obj) {
                            canMsgNotPublishDatingDialog.dismiss();
                        }

                        @Override
                        public void onCancel(View view, Object... obj) {
                            canMsgNotPublishDatingDialog.dismiss();
                        }
                    });

                    canMsgNotPublishDatingDialog.show();


                    return;

                }

                List<DatingRequirment> list = resp.getRequirements();

                boolean haveNotIsReady = false;

                if (null != list && list.size() > 0) {

                    for (int i = 0; i < list.size(); i++) {

                        if (list.get(i).isReady()) {
                            haveNotIsReady = true;
                            break;
                        }


                    }

                    if (haveNotIsReady) {
                        canNotPublishDatingDialog = DialogUtil.createNotCanPublishDatingHintDialog(ReleaseAppointmentActivity.this, list, "前往查看", new BackCallListener() {
                            @Override
                            public void onEnsure(View view, Object... obj) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
                                ActivityUtil.jump(ReleaseAppointmentActivity.this, UserInfoActivity.class, bundle, 0, 100);
                            }

                            @Override
                            public void onCancel(View view, Object... obj) {

                            }
                        });
                        canNotPublishDatingDialog.show();
                    } else {

                        if (TextUtils.equals("UserAppointListActivity", frompage)) {

                            RxBus.get().post("DatingsRefreshEvent",new DatingsRefreshEvent(2));

                        } else if (TextUtils.equals("AppointmentFragment", frompage)) {

                            RxBus.get().post("DatingsRefreshEvent",new DatingsRefreshEvent(1));
                        }


                        finish();

                    }

                } else {

                    if (TextUtils.equals("UserAppointListActivity", frompage)) {

                        RxBus.get().post("DatingsRefreshEvent",new DatingsRefreshEvent(2));

                    } else if (TextUtils.equals("AppointmentFragment", frompage)) {

                        RxBus.get().post("DatingsRefreshEvent",new DatingsRefreshEvent(1));
                    }

                    finish();
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();
                isRepeat = false;

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this);

                } else {
                    DialogUtil.showDisCoverNetToast(ReleaseAppointmentActivity.this, msg);
                }

            }
        });

        datingsService.enqueue();

    }


}
