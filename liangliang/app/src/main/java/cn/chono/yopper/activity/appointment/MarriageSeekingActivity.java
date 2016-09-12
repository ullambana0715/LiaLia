package cn.chono.yopper.activity.appointment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingDetail.DatingDetailBean;
import cn.chono.yopper.Service.Http.DatingDetail.DatingDetailRespBean;
import cn.chono.yopper.Service.Http.DatingDetail.DatingDetailService;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsBean;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsData;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsRespBean;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsService;
import cn.chono.yopper.Service.Http.DatingPublish.Marriage;
import cn.chono.yopper.Service.Http.DatingsModify.DatingsModifyRespBean;
import cn.chono.yopper.Service.Http.DatingsModify.DatingsModifyService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageRespBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageService;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.event.DatingsRefreshEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.InfoTransformUtils;
import cn.chono.yopper.view.WheelDialog.LocationWheelDialog;
import cn.chono.yopper.view.WheelDialog.OnWheelListener;
import cn.chono.yopper.view.WheelDialog.RandomWheelDialog;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 征婚
 * Created by cc on 16/3/24.
 */
public class MarriageSeekingActivity extends MainFrameActivity implements View.OnClickListener {

    private ImageView personals_image_iv;

    private LinearLayout personals_image_layout;

    private TextView personals_marry_tv, personals_parents_tv, personals_only_child_tv, personals_census_tv, personals_address_tv, personals_height_tv, personals_body_weight_tv, personals_occupation_tv, personals_monthly_income_tv, personals_educational_background_tv, personals_romance_tv, personals_marital_history_tv, personals_have_children_tv, personals_drinking_tv, personals_health_tv, personals_housing_tv, personals_send_message_tv;


    private int APPOINT_TYPE;
    private int userId, user_sex;


    private Dialog photoDialog;


    private String filePath;


    private Resources res;


    private Dialog wheelDialog;

    private Dialog hint_dialog, hint_filePath_dialog, canNotPublishDatingDialog, canMsgNotPublishDatingDialog;

    private String[] marryArray, parentsArray, only_childArray, educational_backgroundArray, romanceArray, marital_historyArray, have_childrenArray, healthArray, housingArray, heightArray, body_weightArray, monthly_incomeArray, drinkingArray;


    private String marry, parents, only_child, educational_background, romance, marital_history, have_children, health, housing, height, body_weight, monthly_income;
    private String occupation, send_message;

    private String strCensusProvince;
    private String strCensusCity;
    private String strAddressProvince;
    private String strAddressCity;

    private String drinking1, drinking2, drinking3;

    private Dialog loadingDiaog;

    private boolean isRepeat = false;
    private boolean isputimage = false;

    private String frompage;

    private String datingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.personals_activity);
        initView();
        initLoadingDialog();
        initDataHint();
        setListener();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(YpSettings.APPOINTMENT_INTENT_YTPE))
            APPOINT_TYPE = bundle.getInt(YpSettings.APPOINTMENT_INTENT_YTPE);

        if (bundle.containsKey("datingId"))
            datingId = bundle.getString("datingId");

        frompage = bundle.getString(YpSettings.FROM_PAGE);

        getDatingDetail();

    }


    private void getDatingDetail() {
        if (TextUtils.isEmpty(datingId)) {
            return;
        }


        double latitude = 0, longtitude = 0;


        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            latitude = myLoc.getLoc().getLatitude();
            longtitude = myLoc.getLoc().getLongitude();


        }
        LatLng pt = new LatLng(latitude, longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);


        DatingDetailBean datingDetailBean = new DatingDetailBean();

        datingDetailBean.setLat(pt.latitude);

        datingDetailBean.setLng(pt.longitude);

        datingDetailBean.setDatingId(datingId);


        DatingDetailService datingDetailService = new DatingDetailService(this);

        datingDetailService.parameter(datingDetailBean);

        datingDetailService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DatingDetailRespBean datingDetailRespBean = (DatingDetailRespBean) respBean;

                AppointDetailDto appointDetailDto = datingDetailRespBean.getResp();

                if (null == appointDetailDto) return;

                Marriage marriage = appointDetailDto.getMarriage();


                if (null == marriage) return;

                filePath = marriage.getPhotoUrl();

                Glide.with(MarriageSeekingActivity.this).load(filePath).into(personals_image_iv);

                marry = InfoTransformUtils.getPublishWishMarriageTime(marriage.getWishMarriageTime());
                personals_marry_tv.setText(marry);

                parents = InfoTransformUtils.getParentsBeingAlive(marriage.getParentsBeingAlive());
                personals_parents_tv.setText(parents);


                if (marriage.isOnlyChild()) {
                    only_child = "是";
                } else {
                    only_child = "否";
                }
                personals_only_child_tv.setText(only_child);


                strCensusProvince = marriage.getPermanentFirstArea();
                strCensusCity = marriage.getPermanentSecondArea();
                StringBuffer sb = new StringBuffer();
                sb.append(strCensusProvince);
                sb.append(" ");
                sb.append(strCensusCity);
                personals_census_tv.setText(sb.toString().trim());


                strAddressProvince = marriage.getPresentFirstArea();
                strAddressCity = marriage.getPresentSecondArea();
                StringBuffer sbs = new StringBuffer();
                sbs.append(strAddressProvince);
                sbs.append(" ");
                sbs.append(strAddressCity);
                personals_address_tv.setText(sbs.toString().trim());


                StringBuffer heightSb = new StringBuffer();
                heightSb.append(marriage.getHeight());
                heightSb.append("cm");
                height = heightSb.toString().trim();
                personals_height_tv.setText(height);


                StringBuffer weightSb = new StringBuffer();
                weightSb.append(marriage.getWeight());
                weightSb.append("kg");
                body_weight = weightSb.toString().trim();
                personals_body_weight_tv.setText(body_weight);


                occupation = marriage.getProfession();
                personals_occupation_tv.setText(occupation);


                monthly_income = InfoTransformUtils.getPersonalsIncome(marriage.getIncome());
                personals_monthly_income_tv.setText(monthly_income);


                educational_background = InfoTransformUtils.getEducation(marriage.getEducation());
                personals_educational_background_tv.setText(educational_background);


                romance = InfoTransformUtils.getLoveHistory(marriage.getLoveHistory());
                personals_romance_tv.setText(romance);


                if (marriage.isHasMarriageHistory()) {
                    marital_history = "是";
                } else {
                    marital_history = "否";
                }
                personals_marital_history_tv.setText(marital_history);


                have_children = InfoTransformUtils.getChildrenCondition(marriage.getChildrenCondition());
                personals_have_children_tv.setText(have_children);


                int[] drinkConditions = marriage.getDrinkConditions();

                if (null != drinkConditions && drinkConditions.length > 0) {

                    for (int i = 0; i < drinkConditions.length; i++) {
                        switch (i) {

                            case 0:
                                drinking1 = InfoTransformUtils.getDrinkConditions(drinkConditions[i]);
                                break;

                            case 1:
                                drinking2 = InfoTransformUtils.getDrinkConditions(drinkConditions[i]);
                                break;

                            case 2:
                                drinking3 = InfoTransformUtils.getDrinkConditions(drinkConditions[i]);
                                break;
                        }


                    }

                }
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(drinking1);
                if (!TextUtils.isEmpty(drinking2)) {
                    stringBuffer.append(",");
                    stringBuffer.append(drinking2);
                }
                if (!TextUtils.isEmpty(drinking3)) {
                    stringBuffer.append(",");
                    stringBuffer.append(drinking3);

                }
                personals_drinking_tv.setText(stringBuffer.toString());


                health = InfoTransformUtils.getHealthCondition(marriage.getHealthCondition());
                personals_health_tv.setText(health);


                housing = InfoTransformUtils.getMarriedHouseCondition(marriage.getMarriedHouseCondition());
                personals_housing_tv.setText(housing);

                send_message = marriage.getWish();
                personals_send_message_tv.setText(send_message);


            }
        });

        datingDetailService.enqueue();

    }

    private void initView() {


        getBtnGoBack().setVisibility(View.GONE);

        TextView tvBack = gettvBack();
        tvBack.setVisibility(View.VISIBLE);
        tvBack.setText("取消");

        TextView tvOption = gettvOption();
        tvOption.setVisibility(View.VISIBLE);
        tvOption.setText("发布");
        tvOption.setTextColor(getResources().getColor(R.color.color_ff7462));

        getTvTitle().setText("发布征婚");

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


        res = getResources();

        personals_image_iv = (ImageView) findViewById(R.id.personals_image_iv);

        personals_image_layout = (LinearLayout) findViewById(R.id.personals_image_layout);

        personals_marry_tv = (TextView) findViewById(R.id.personals_marry_tv);
        personals_parents_tv = (TextView) findViewById(R.id.personals_parents_tv);
        personals_only_child_tv = (TextView) findViewById(R.id.personals_only_child_tv);
        personals_census_tv = (TextView) findViewById(R.id.personals_census_tv);
        personals_address_tv = (TextView) findViewById(R.id.personals_address_tv);
        personals_height_tv = (TextView) findViewById(R.id.personals_height_tv);
        personals_body_weight_tv = (TextView) findViewById(R.id.personals_body_weight_tv);
        personals_occupation_tv = (TextView) findViewById(R.id.personals_occupation_tv);
        personals_monthly_income_tv = (TextView) findViewById(R.id.personals_monthly_income_tv);
        personals_educational_background_tv = (TextView) findViewById(R.id.personals_educational_background_tv);
        personals_romance_tv = (TextView) findViewById(R.id.personals_romance_tv);
        personals_marital_history_tv = (TextView) findViewById(R.id.personals_marital_history_tv);
        personals_have_children_tv = (TextView) findViewById(R.id.personals_have_children_tv);
        personals_drinking_tv = (TextView) findViewById(R.id.personals_drinking_tv);
        personals_health_tv = (TextView) findViewById(R.id.personals_health_tv);
        personals_housing_tv = (TextView) findViewById(R.id.personals_housing_tv);
        personals_send_message_tv = (TextView) findViewById(R.id.personals_send_message_tv);

    }


    /**
     * 设置头像是对dialog的初始化
     */
    private void initLoadingDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout loadingview = (LinearLayout) inflater.inflate(R.layout.loading_dialog, null);

        loadingDiaog = DialogUtil.hineDialog(MarriageSeekingActivity.this, loadingview);
    }


    private void initDataHint() {
        hint_dialog = DialogUtil.createHintOperateDialog(MarriageSeekingActivity.this, "", getString(R.string.text_appointment_personals_hini), "", "确定", new BackCallListener() {

            @Override
            public void onEnsure(View view, Object... obj) {
                hint_dialog.dismiss();
            }

            @Override
            public void onCancel(View view, Object... obj) {
                hint_dialog.dismiss();
            }
        });
    }

    private void setListener() {

        personals_image_iv.setOnClickListener(this);
        personals_image_layout.setOnClickListener(this);
        personals_marry_tv.setOnClickListener(this);
        personals_parents_tv.setOnClickListener(this);
        personals_only_child_tv.setOnClickListener(this);
        personals_census_tv.setOnClickListener(this);
        personals_address_tv.setOnClickListener(this);
        personals_height_tv.setOnClickListener(this);
        personals_body_weight_tv.setOnClickListener(this);
        personals_occupation_tv.setOnClickListener(this);
        personals_monthly_income_tv.setOnClickListener(this);
        personals_educational_background_tv.setOnClickListener(this);
        personals_romance_tv.setOnClickListener(this);
        personals_marital_history_tv.setOnClickListener(this);
        personals_have_children_tv.setOnClickListener(this);
        personals_drinking_tv.setOnClickListener(this);
        personals_health_tv.setOnClickListener(this);
        personals_housing_tv.setOnClickListener(this);
        personals_send_message_tv.setOnClickListener(this);


    }


    private void MultiImageSelector() {


        MultiImageSelector.create()

                .showCamera(true) // 是否显示相机. 默认为显示

//                .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式

//                .multi() // 多选模式, 默认模式;
//            .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效

                .start(MarriageSeekingActivity.this, YpSettings.ICON_REQUEST_IMAGE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personals_image_iv://照片

//                MultiImageSelector(); //// TODO: 16/8/17 需求上没有事件 

                break;
            case R.id.personals_image_layout://上传照片

                MultiImageSelector();

                break;
            case R.id.personals_marry_tv://你想何时结婚

                if (null == marryArray)
                    marryArray = res.getStringArray(R.array.marry);


                wheelDialog = new RandomWheelDialog(this, marryArray, marry, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        marry = oneContext;
                        personals_marry_tv.setText(marry);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();


                break;
            case R.id.personals_parents_tv://你的父母是否建在

                if (null == parentsArray)
                    parentsArray = res.getStringArray(R.array.parents);

                wheelDialog = new RandomWheelDialog(this, parentsArray, parents, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        parents = oneContext;
                        personals_parents_tv.setText(parents);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();


                break;
            case R.id.personals_only_child_tv://你是否独生子女

                if (null == only_childArray)
                    only_childArray = res.getStringArray(R.array.only_child);

                wheelDialog = new RandomWheelDialog(this, only_childArray, only_child, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        only_child = oneContext;
                        personals_only_child_tv.setText(only_child);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_census_tv://户籍所在地

                wheelDialog = new LocationWheelDialog(this, strCensusProvince, strCensusCity, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        personals_census_tv.setText(oneContext);

                        strCensusProvince = oneContext;

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                        strCensusProvince = oneContext;
                        strCensusCity = twoContext;


                        StringBuffer sb = new StringBuffer();
                        sb.append(oneContext);
                        sb.append(" ");
                        sb.append(twoContext);

                        personals_census_tv.setText(sb);

                    }
                });

                wheelDialog.show();
                break;
            case R.id.personals_address_tv://现居地

                wheelDialog = new LocationWheelDialog(this, strAddressProvince, strAddressCity, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        personals_address_tv.setText(oneContext);
                        strAddressProvince = oneContext;

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                        strAddressProvince = oneContext;
                        strAddressCity = twoContext;

                        StringBuffer sb = new StringBuffer();
                        sb.append(oneContext);
                        sb.append(" ");
                        sb.append(twoContext);

                        personals_address_tv.setText(sb);

                    }
                });

                wheelDialog.show();


                break;
            case R.id.personals_height_tv://身高

                if (null == heightArray)
                    heightArray = res.getStringArray(R.array.height);

                wheelDialog = new RandomWheelDialog(this, heightArray, height, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        height = oneContext;
                        personals_height_tv.setText(height);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_body_weight_tv://体重

                if (null == body_weightArray)
                    body_weightArray = res.getStringArray(R.array.body_weight);

                wheelDialog = new RandomWheelDialog(this, body_weightArray, body_weight, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        body_weight = oneContext;
                        personals_body_weight_tv.setText(body_weight);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();


                break;
            case R.id.personals_occupation_tv://职业

                Bundle fitness = new Bundle();
                fitness.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MARRIED);
                fitness.putString("content", occupation);
                fitness.putInt("type", 2);

                Intent fitnessInt = new Intent(MarriageSeekingActivity.this, ReleaseAppointmentThemeActivity.class);

                fitnessInt.putExtras(fitness);

                startActivityForResult(fitnessInt, YpSettings.MOVEMENT_THEME);


                break;
            case R.id.personals_monthly_income_tv://月收入

                if (null == monthly_incomeArray)
                    monthly_incomeArray = res.getStringArray(R.array.monthly_income);

                wheelDialog = new RandomWheelDialog(this, monthly_incomeArray, monthly_income, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        monthly_income = oneContext;
                        personals_monthly_income_tv.setText(monthly_income);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();
                break;
            case R.id.personals_educational_background_tv://学历


                if (null == educational_backgroundArray)
                    educational_backgroundArray = res.getStringArray(R.array.educational_background);

                wheelDialog = new RandomWheelDialog(this, educational_backgroundArray, educational_background, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        educational_background = oneContext;
                        personals_educational_background_tv.setText(educational_background);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_romance_tv://既往恋爱史


                if (null == romanceArray)
                    romanceArray = res.getStringArray(R.array.romance);

                wheelDialog = new RandomWheelDialog(this, romanceArray, romance, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        romance = oneContext;
                        personals_romance_tv.setText(romance);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_marital_history_tv://有无婚史


                if (null == marital_historyArray)
                    marital_historyArray = res.getStringArray(R.array.marital_history);

                wheelDialog = new RandomWheelDialog(this, marital_historyArray, marital_history, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        marital_history = oneContext;
                        personals_marital_history_tv.setText(marital_history);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_have_children_tv://有无子女

                if (null == have_childrenArray)
                    have_childrenArray = res.getStringArray(R.array.have_children);

                wheelDialog = new RandomWheelDialog(this, have_childrenArray, have_children, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        have_children = oneContext;
                        personals_have_children_tv.setText(have_children);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_drinking_tv://饮酒

                //drinking


                Bundle drinkingBu = new Bundle();
                drinkingBu.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MARRIED);


                drinkingBu.putString("drinking1", drinking1);
                drinkingBu.putString("drinking2", drinking2);
                drinkingBu.putString("drinking3", drinking3);

                Intent drinkingInt = new Intent(MarriageSeekingActivity.this, DrinkingOptionsActivity.class);

                drinkingInt.putExtras(drinkingBu);

                startActivityForResult(drinkingInt, YpSettings.MOVEMENT_DRINKING);


                break;
            case R.id.personals_health_tv://健康自评

                if (null == healthArray)
                    healthArray = res.getStringArray(R.array.health);

                wheelDialog = new RandomWheelDialog(this, healthArray, health, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        health = oneContext;
                        personals_health_tv.setText(health);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_housing_tv://婚后住房


                if (null == housingArray)
                    housingArray = res.getStringArray(R.array.housing);

                wheelDialog = new RandomWheelDialog(this, housingArray, housing, new OnWheelListener() {
                    @Override
                    public void onWeelListener(String oneContext) {
                        housing = oneContext;
                        personals_housing_tv.setText(housing);

                    }

                    @Override
                    public void onWeelListener(String oneContext, String twoContext) {

                    }


                });
                wheelDialog.show();

                break;
            case R.id.personals_send_message_tv://对他的寄语


                Bundle sendBun = new Bundle();

                sendBun.putString("content", send_message);


                Intent sendInt = new Intent(MarriageSeekingActivity.this, WishesForYouActivity.class);

                sendInt.putExtras(sendBun);

                startActivityForResult(sendInt, YpSettings.MOVEMENT_SEND_MESSAGE);


                break;
        }
    }


    private void dataDealWith() {

        if (isRepeat) return;


        if (TextUtils.isEmpty(filePath)) {//putFilePaht地址为空时，才判断本地


            hint_filePath_dialog = DialogUtil.createHintOperateDialog(MarriageSeekingActivity.this, "", getString(R.string.text_personals_image_hint), "", "确定", new BackCallListener() {

                @Override
                public void onEnsure(View view, Object... obj) {
                    hint_filePath_dialog.dismiss();
                }

                @Override
                public void onCancel(View view, Object... obj) {
                    hint_filePath_dialog.dismiss();
                }
            });


            hint_filePath_dialog.show();

        } else if (TextUtils.isEmpty(marry)) {//何时结婚

            hint_dialog.show();

        } else if (TextUtils.isEmpty(parents)) {//父母是否健在
            hint_dialog.show();

        } else if (TextUtils.isEmpty(only_child)) {//你是否独生子女
            hint_dialog.show();

        } else if (TextUtils.isEmpty(strCensusProvince) || TextUtils.isEmpty(strCensusCity)) {//户籍地址

            hint_dialog.show();
        } else if (TextUtils.isEmpty(strAddressProvince) || TextUtils.isEmpty(strAddressCity)) {//现居地
            hint_dialog.show();
        } else if (TextUtils.isEmpty(height)) {//身高
            hint_dialog.show();
        } else if (TextUtils.isEmpty(body_weight)) {//体重
            hint_dialog.show();
        } else if (TextUtils.isEmpty(occupation.trim())) {//职业
            hint_dialog.show();
        } else if (TextUtils.isEmpty(monthly_income)) {//月收入
            hint_dialog.show();
        } else if (TextUtils.isEmpty(educational_background)) {//学历
            hint_dialog.show();
        } else if (TextUtils.isEmpty(romance)) {//恋爱史
            hint_dialog.show();
        } else if (TextUtils.isEmpty(marital_history)) {//婚史
            hint_dialog.show();
        } else if (TextUtils.isEmpty(have_children)) {//子女
            hint_dialog.show();
        } else if (TextUtils.isEmpty(personals_drinking_tv.getText().toString().trim())) {//饮酒
            hint_dialog.show();
        } else if (TextUtils.isEmpty(health)) {//健康自评
            hint_dialog.show();
        } else if (TextUtils.isEmpty(housing)) {//婚后住房
            hint_dialog.show();
        } else if (TextUtils.isEmpty(send_message)) {//寄语
            hint_dialog.show();
        } else {
            isRepeat = true;

            loadingDiaog.show();//启动弹窗

            int marryType = InfoTransformUtils.getPublishWishMarriageTime(marry);
            int parentsType = InfoTransformUtils.getParentsBeingAlive(parents);
            boolean only_childType = only_childData();

            //

            int heightType = heightData();
            int body_weightType = body_weightData();
            //occupation

            int monthly_incomeType = InfoTransformUtils.getPersonalsIncome(monthly_income);
            int educational_backgroundType = InfoTransformUtils.getEducation(educational_background);

            int romanceType = InfoTransformUtils.getLoveHistory(romance);

            boolean marital_historyType = marital_historyData();


            int have_childrenType = InfoTransformUtils.getChildrenCondition(have_children);

            int[] drinkingType = drinkingData();

            int healthType = InfoTransformUtils.getHealthCondition(health);

            int housingType = InfoTransformUtils.getMarriedHouseCondition(housing);
//send_message

            douploadingUserImg(filePath, marryType, parentsType,
                    only_childType, strCensusProvince, strCensusCity, strAddressProvince, strAddressCity,
                    heightType, body_weightType, occupation,
                    monthly_incomeType, educational_backgroundType,
                    romanceType, marital_historyType,
                    have_childrenType, drinkingType, healthType,
                    housingType, send_message);

        }


    }

    /**
     * 上传图片
     *
     * @param @param filePath
     * @param @param date
     * @param @param w
     * @param @param h 设定文件
     * @return void 返回类型
     * @throws
     * @Title: uploadingUserHeadImg
     */

    private void douploadingUserImg(String filePath, final int marryType, final int parentsType,
                                    final boolean only_childType, final String permanentFirstArea, final String permanentSecondArea, final String presentFirstArea, final String presentSecondArea,
                                    final int heightType, final int body_weightType, final String occupation,
                                    final int monthly_incomeType, final int educational_backgroundType,
                                    final int romanceType, final boolean marital_historyType,
                                    final int have_childrenType, final int[] drinkingType, final int healthType,
                                    final int housingType, final String send_message) {


        if (!TextUtils.isEmpty(datingId) && !isputimage) {//是修改并没有图片更换过

            postData(filePath, marryType, parentsType, only_childType, permanentFirstArea,
                    permanentSecondArea, presentFirstArea, presentSecondArea,
                    heightType, body_weightType, occupation,
                    monthly_incomeType, educational_backgroundType,
                    romanceType, marital_historyType,
                    have_childrenType, drinkingType, healthType,
                    housingType, send_message);
            return;
        }

        //不管是修改还是发布。只要有修改过图片就要重新上传


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


                postData(url, marryType, parentsType, only_childType, permanentFirstArea,
                        permanentSecondArea, presentFirstArea, presentSecondArea,
                        heightType, body_weightType, occupation,
                        monthly_incomeType, educational_backgroundType,
                        romanceType, marital_historyType,
                        have_childrenType, drinkingType, healthType,
                        housingType, send_message);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                isRepeat = false;
                loadingDiaog.dismiss();

                String msg = respBean.getMsg();

                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this);
                    return;
                }

                DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this, msg);

            }
        });
        datingsImageService.enqueue();


    }

    private void postData(String imgUrl, int marryType, int parentsType,
                          boolean only_childType, String permanentFirstArea, String permanentSecondArea, String presentFirstArea, String presentSecondArea,
                          int heightType, final int body_weightType, String occupation,
                          int monthly_incomeType, int educational_backgroundType,
                          int romanceType, boolean marital_historyType,
                          int have_childrenType, int[] drinkingType, int healthType,
                          int housingType, String send_message) {

        double latitude = 0, longtitude = 0;

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

        datingsBean.setActivityType(Constant.APPOINT_TYPE_MARRIED);

        Marriage marriage = new Marriage();
        marriage.setWishMarriageTime(marryType);
        marriage.setParentsBeingAlive(parentsType);
        marriage.setOnlyChild(only_childType);
        marriage.setPermanentFirstArea(permanentFirstArea);
        marriage.setPermanentSecondArea(permanentSecondArea);
        marriage.setPresentFirstArea(presentFirstArea);
        marriage.setPresentSecondArea(presentSecondArea);
        marriage.setHeight(heightType);
        marriage.setWeight(body_weightType);
        marriage.setProfession(occupation);
        marriage.setIncome(monthly_incomeType);
        marriage.setEducation(educational_backgroundType);
        marriage.setLoveHistory(romanceType);
        marriage.setHasMarriageHistory(marital_historyType);
        marriage.setChildrenCondition(have_childrenType);
        marriage.setDrinkConditions(drinkingType);
        marriage.setHealthCondition(healthType);
        marriage.setHealthCondition(housingType);
        marriage.setWish(send_message);
        marriage.setPhotoUrl(imgUrl);


        datingsBean.setMarriage(marriage);


        if (!TextUtils.isEmpty(datingId)) {//修改

            datingsBean.setDatingId(datingId);

            DatingsModifyService datingsModifyService = new DatingsModifyService(this);

            datingsModifyService.parameter(datingsBean);

            datingsModifyService.callBack(new OnCallBackSuccessListener() {
                @Override
                public void onSuccess(RespBean respBean) {
                    super.onSuccess(respBean);

                    loadingDiaog.dismiss();
                    isRepeat = false;

                    DatingsModifyRespBean datingsModifyRespBean = (DatingsModifyRespBean) respBean;


                    if (datingsModifyRespBean.isResp()) {

                        getUserInfo(LoginUser.getInstance().getUserId());

                        if (TextUtils.equals("UserAppointListActivity", frompage)) {

                            RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(2));

                        } else if (TextUtils.equals("DatingDetailActivity", frompage)) {

                            Intent intent = new Intent(MarriageSeekingActivity.this, DatingDetailActivity.class);
                            setResult(YpSettings.DATINGS_TO_MARRIAGE, intent);

                        } else if (TextUtils.equals("AppointmentFragment", frompage)) {
                            LogUtils.e("来了征婚");
                            RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(1));

                        }


                        finish();

                        return;
                    }

                    DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this, "提交失败");


                }
            }, new OnCallBackFailListener() {
                @Override
                public void onFail(RespBean respBean) {
                    super.onFail(respBean);

                    loadingDiaog.dismiss();
                    isRepeat = false;

                    String msg = respBean.getMsg();
                    if (TextUtils.isEmpty(msg)) {
                        DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this);

                    } else {
                        DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this, msg);
                    }


                }
            });

            datingsModifyService.enqueue();


            return;
        }

        //发布


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

                    if (TextUtils.equals("UserAppointListActivity", frompage)) {
                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(2));

                    } else if (TextUtils.equals("AppointmentFragment", frompage)) {
                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(1));

                    }

                    finish();

                    return;
                }


                String limitMsg = resp.getLimitMsg();

                if (!TextUtils.isEmpty(limitMsg)) {


                    canMsgNotPublishDatingDialog = DialogUtil.createHintOperateDialog(MarriageSeekingActivity.this, "", limitMsg, "", "确定", new BackCallListener() {

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

                        canNotPublishDatingDialog = DialogUtil.createNotCanPublishDatingHintDialog(MarriageSeekingActivity.this, list, "前往查看", new BackCallListener() {
                            @Override
                            public void onEnsure(View view, Object... obj) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());

                                ActivityUtil.jump(MarriageSeekingActivity.this, UserInfoActivity.class, bundle, 0, 100);
                            }

                            @Override
                            public void onCancel(View view, Object... obj) {

                            }
                        });
                        canNotPublishDatingDialog.show();
                    } else {

                        if (TextUtils.equals("UserAppointListActivity", frompage)) {

                            RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(2));

                        } else if (TextUtils.equals("AppointmentFragment", frompage)) {

                            RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(1));

                        }

                        finish();

                    }

                } else {

                    if (TextUtils.equals("UserAppointListActivity", frompage)) {

                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(2));

                    } else if (TextUtils.equals("AppointmentFragment", frompage)) {

                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(1));
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

                    DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this);

                } else {

                    DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this, msg);
                }

            }
        });

        datingsService.enqueue();


    }


    private boolean only_childData() {

        boolean only_childType = true;

        if (TextUtils.equals("是", only_child)) {

            only_childType = true;

        } else if (TextUtils.equals("否", only_child)) {

            only_childType = false;
        }
        return only_childType;

    }

    private int heightData() {

        int heightType = 0;

        if (!TextUtils.isEmpty(height)) {

            heightType = Integer.valueOf(height.replace("cm", ""));
        }

        return heightType;
    }

    private int body_weightData() {

        int body_weightType = 0;

        if (!TextUtils.isEmpty(body_weight)) {

            body_weightType = Integer.valueOf(body_weight.replace("kg", ""));
        }

        return body_weightType;
    }


    private boolean marital_historyData() {

        boolean marital_historyType = false;

        if (TextUtils.equals("无", marital_history)) {

            marital_historyType = false;

        } else if (TextUtils.equals("有", marital_history)) {

            marital_historyType = true;
        }

        return marital_historyType;
    }


    private int[] drinkingData() {


        Integer drinking1Type = InfoTransformUtils.getDrinkConditions(drinking1);

        Integer drinking2Type = InfoTransformUtils.getDrinkConditions(drinking2);

        Integer drinking3Type = InfoTransformUtils.getDrinkConditions(drinking3);



        ArrayList<Integer> drinkingList = new ArrayList<>();

        if (null != drinking1Type) {

            drinkingList.add(drinking1Type);
        }

        if (null != drinking2Type) {

            drinkingList.add(drinking2Type);

        }

        if (null != drinking3Type) {

            drinkingList.add(drinking3Type);
        }

        int[] drinkings = new int[drinkingList.size()];

        for (int i = 0; i < drinkingList.size(); i++) {

            drinkings[i] = drinkingList.get(i);
        }

        return drinkings;


    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {


            case YpSettings.MOVEMENT_DRINKING:
                if (resultCode == YpSettings.MOVEMENT_DRINKING) {

                    if (data != null) {

                        drinking1 = data.getExtras().getString("drinking1");

                        drinking2 = data.getExtras().getString("drinking2");

                        drinking3 = data.getExtras().getString("drinking3");

                        StringBuffer stringBuffer = new StringBuffer();

                        stringBuffer.append(drinking1);


                        if (!TextUtils.isEmpty(drinking2)) {

                            stringBuffer.append(",");

                            stringBuffer.append(drinking2);


                        }
                        if (!TextUtils.isEmpty(drinking3)) {

                            stringBuffer.append(",");

                            stringBuffer.append(drinking3);

                        }


                        personals_drinking_tv.setText(stringBuffer.toString());

                    }

                }


                break;
            case YpSettings.MOVEMENT_SEND_MESSAGE:

                if (resultCode == YpSettings.MOVEMENT_SEND_MESSAGE) {

                    if (data != null) {

                        send_message = (String) data.getExtras().getSerializable("content");

                        if (!TextUtils.isEmpty(send_message)) {

                            personals_send_message_tv.setText(send_message);
                        }


                    }

                }


                break;
            case YpSettings.MOVEMENT_THEME:

                if (resultCode == YpSettings.MOVEMENT_THEME) {

                    if (data != null) {

                        occupation = (String) data.getExtras().getSerializable("content");

                        if (!TextUtils.isEmpty(occupation)) {

                            personals_occupation_tv.setText(occupation);

                        } else {

                            personals_occupation_tv.setText("");

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

        Glide.with(this).load(file_Path).into(personals_image_iv);

        Bitmap bm = ImgUtils.resizesBitmap(file_Path);

        if (null != bm) {
            // 保存在自己定义文件的路径

            filePath = ImgUtils.saveImgFile(this, bm);
            bm.recycle();
            if (!CheckUtil.isEmpty(filePath)) {

                if (!TextUtils.isEmpty(datingId))

                    isputimage = true;


            } else {
                DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this, "选取失败，请重新选择上传！");
            }
        } else {
            DialogUtil.showDisCoverNetToast(MarriageSeekingActivity.this, "选取失败，请重新选择上传！");
        }

    }


}
