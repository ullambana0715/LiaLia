package cn.chono.yopper.activity.appointment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsBean;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsData;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsRespBean;
import cn.chono.yopper.Service.Http.DatingPublish.DatingsService;
import cn.chono.yopper.Service.Http.DatingPublish.Travel;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageRespBean;
import cn.chono.yopper.Service.Http.UploadingDatingsImage.UploadingDatingsImageService;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.TravelLabelSetect;
import cn.chono.yopper.data.TravelObjects;
import cn.chono.yopper.data.TravelPay;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.event.DatingsRefreshEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.BackTravelCall;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.InfoTransformUtils;
import cn.chono.yopper.view.FlowLeftLayout;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 旅行发布
 */
public class TravelActivity extends MainFrameActivity implements View.OnClickListener {


    private TextView travel_layout_destination_tv;

    private LinearLayout travel_layout_label;

    private TextView travel_layout_lael_tv;

    private FlowLeftLayout travel_layout_label_lable_flowlayout;


    private TextView travel_layout_objects_tv;


    private TextView travel_layout_departure_time_tv;


    private TextView travel_layout_estimated_time_tv;


    private TextView travel_layout_travel_tv;


    private TextView travel_layout_pay_tv;

    private EditText appointment_write_et;

    private TextView appointment_write_et_number_tv;

    private ImageView appointment_write_image_iv, appointment_write_image_fillet_delete_iv;

    private String mDescriptionContent;


    private Dialog photoDialog;


    private List<TravelLabelSetect> significanceSetects;
    private List<TravelLabelSetect> preferencesSetects;

    private List<TravelLabelSetect> addSetectsList;

    private TravelObjects mTravelObjects;

    private TravelPay mTravelPay;

    private Dialog departure_time_dialog, estimated_time, canNotPublishDatingDialog, canMsgNotPublishDatingDialog;

    private String destinationContext;


    private int days;

    private int month;

    private int Date;

    private String filePath;

    private String meetingTravelTimeTypeStr;

    private String planTimeStr;

    private Dialog loadingDiaog;

    private boolean isRepeat = false;

    private String frompage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.travel_activity);

        initView();

        initLoadingDialog();

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("content")) {
            destinationContext = bundle.getString("content");
            travel_layout_destination_tv.setText(destinationContext);
        }

        if (bundle.containsKey(YpSettings.FROM_PAGE))
            frompage = bundle.getString(YpSettings.FROM_PAGE);

        mTravelPay = new TravelPay();
        significanceSetects = new ArrayList<>();
        preferencesSetects = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initView() {

        getTvTitle().setText("旅行");

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


        travel_layout_destination_tv = (TextView) findViewById(R.id.travel_layout_destination_tv);

        travel_layout_label = (LinearLayout) findViewById(R.id.travel_layout_label);

        travel_layout_lael_tv = (TextView) findViewById(R.id.travel_layout_lael_tv);

        travel_layout_label_lable_flowlayout = (FlowLeftLayout) findViewById(R.id.travel_layout_label_lable_flowlayout);


        travel_layout_objects_tv = (TextView) findViewById(R.id.travel_layout_objects_tv);


        travel_layout_departure_time_tv = (TextView) findViewById(R.id.travel_layout_departure_time_tv);


        travel_layout_estimated_time_tv = (TextView) findViewById(R.id.travel_layout_estimated_time_tv);


        travel_layout_travel_tv = (TextView) findViewById(R.id.travel_layout_travel_tv);


        travel_layout_pay_tv = (TextView) findViewById(R.id.travel_layout_pay_tv);

        appointment_write_et = (EditText) findViewById(R.id.appointment_write_et);

        appointment_write_et_number_tv = (TextView) findViewById(R.id.appointment_write_et_number_tv);

        appointment_write_image_iv = (ImageView) findViewById(R.id.appointment_write_image_iv);

        appointment_write_image_fillet_delete_iv = (ImageView) findViewById(R.id.appointment_write_image_fillet_delete_iv);


        travel_layout_destination_tv.setOnClickListener(this);
        travel_layout_label.setOnClickListener(this);
        travel_layout_objects_tv.setOnClickListener(this);
        travel_layout_departure_time_tv.setOnClickListener(this);
        travel_layout_estimated_time_tv.setOnClickListener(this);
        travel_layout_travel_tv.setOnClickListener(this);
        travel_layout_pay_tv.setOnClickListener(this);
        appointment_write_image_iv.setOnClickListener(this);
        appointment_write_image_fillet_delete_iv.setOnClickListener(this);


        appointment_write_et.addTextChangedListener(new AddTextWatcher());


    }

    /**
     * 设置头像是对dialog的初始化
     */
    private void initLoadingDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout loadingview = (LinearLayout) inflater.inflate(R.layout.loading_dialog, null);

        loadingDiaog = DialogUtil.hineDialog(TravelActivity.this, loadingview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.travel_layout_destination_tv://目的地

                Bundle desbun = new Bundle();

                Intent intent = new Intent(this, TravelDestinationActivity.class);
                desbun.putInt("type", 1);
                intent.putExtras(desbun);
                startActivityForResult(intent, YpSettings.TRAVE_DESTINATION);


                break;
            case R.id.travel_layout_label://标签
                Intent it = new Intent(this, TravelLabelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("significanceSetects", (Serializable) significanceSetects);
                bundle.putSerializable("preferencesSetects", (Serializable) preferencesSetects);
                it.putExtras(bundle);
                startActivityForResult(it, YpSettings.TRAVE_LABEL);

                break;
            case R.id.travel_layout_objects_tv://对象

                Intent objectsIt = new Intent(this, TravelObjectsActivity.class);
                Bundle objectsItBd = new Bundle();
                objectsItBd.putSerializable("mTravelObjects", (Serializable) mTravelObjects);
                objectsIt.putExtras(objectsItBd);
                startActivityForResult(objectsIt, YpSettings.TRAVE_OBJECTS);

                break;
            case R.id.travel_layout_departure_time_tv://出发时间

                departure_time_dialog = DialogUtil.createTravelDialog(TravelActivity.this, "计划日期", "近期出发", "某个周末", "具体日期", "可商议", "说走就走", null, null, new BackTravelCall() {
                    @Override
                    public void onOneLayout(View view, Object... obj) {
                        departure_time_dialog.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_departure_time_tv.setText(v.getText());
                        meetingTravelTimeTypeStr = (String) v.getText();
                    }

                    @Override
                    public void onTwoLayout(View view, Object... obj) {
                        departure_time_dialog.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_departure_time_tv.setText(v.getText());
                        meetingTravelTimeTypeStr = (String) v.getText();
                    }

                    @Override
                    public void onThreeLayout(View view, Object... obj) {
                        departure_time_dialog.dismiss();

                        TextView v = (TextView) view;

                        meetingTravelTimeTypeStr = (String) v.getText();

                        Intent timeIt = new Intent(TravelActivity.this, SpecificTimeActivity.class);
                        Bundle timeItBd = new Bundle();
                        timeItBd.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_TRAVEL);

                        timeItBd.putInt("days", days);
                        timeItBd.putInt("month", month);
                        timeItBd.putInt("Date", Date);

                        timeIt.putExtras(timeItBd);
                        startActivityForResult(timeIt, YpSettings.MOVEMENT_TIME);

                    }

                    @Override
                    public void onFourLayout(View view, Object... obj) {
                        departure_time_dialog.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_departure_time_tv.setText(v.getText());
                        meetingTravelTimeTypeStr = (String) v.getText();
                    }

                    @Override
                    public void onFivesLayout(View view, Object... obj) {
                        departure_time_dialog.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_departure_time_tv.setText(v.getText());
                        meetingTravelTimeTypeStr = (String) v.getText();
                    }

                    @Override
                    public void onSixLayout(View view, Object... obj) {
                        departure_time_dialog.dismiss();
                    }

                    @Override
                    public void onSevenLayout(View view, Object... obj) {
                        departure_time_dialog.dismiss();
                    }
                });

                if (!isFinishing()) {
                    departure_time_dialog.show();
                }

                break;
            case R.id.travel_layout_estimated_time_tv://预计时间

                estimated_time = DialogUtil.createTravelDialog(TravelActivity.this, "计划日期", "当天往返", "一两天", "两三天", "三五天", "十天半个月", "还准备回来吗？", "会不会回不来了?", new BackTravelCall() {
                    @Override
                    public void onOneLayout(View view, Object... obj) {
                        estimated_time.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_estimated_time_tv.setText(v.getText());
                        planTimeStr = (String) v.getText();
                    }

                    @Override
                    public void onTwoLayout(View view, Object... obj) {
                        estimated_time.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_estimated_time_tv.setText(v.getText());
                        planTimeStr = (String) v.getText();
                    }

                    @Override
                    public void onThreeLayout(View view, Object... obj) {
                        estimated_time.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_estimated_time_tv.setText(v.getText());
                        planTimeStr = (String) v.getText();
                    }

                    @Override
                    public void onFourLayout(View view, Object... obj) {
                        estimated_time.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_estimated_time_tv.setText(v.getText());
                        planTimeStr = (String) v.getText();
                    }

                    @Override
                    public void onFivesLayout(View view, Object... obj) {
                        estimated_time.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_estimated_time_tv.setText(v.getText());
                        planTimeStr = (String) v.getText();
                    }

                    @Override
                    public void onSixLayout(View view, Object... obj) {
                        estimated_time.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_estimated_time_tv.setText(v.getText());
                        planTimeStr = (String) v.getText();
                    }

                    @Override
                    public void onSevenLayout(View view, Object... obj) {
                        estimated_time.dismiss();
                        TextView v = (TextView) view;
                        travel_layout_estimated_time_tv.setText(v.getText());
                        planTimeStr = (String) v.getText();
                    }
                });

                if (!isFinishing()) {
                    estimated_time.show();
                }

                break;
            case R.id.travel_layout_travel_tv://出行

                Intent sIntent = new Intent(this, TravelPayActivity.class);
                Bundle sBd = new Bundle();
                sBd.putSerializable("mTravelPay", (Serializable) mTravelPay);
                sIntent.putExtras(sBd);
                startActivityForResult(sIntent, YpSettings.TRAVE_PAY);

                break;
            case R.id.travel_layout_pay_tv://买单

                Intent payIntent = new Intent(this, TravelPayActivity.class);
                Bundle patBd = new Bundle();
                patBd.putSerializable("mTravelPay", (Serializable) mTravelPay);
                payIntent.putExtras(patBd);
                startActivityForResult(payIntent, YpSettings.TRAVE_PAY);

                break;
            case R.id.appointment_write_image_iv://图片添加


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
            case R.id.appointment_write_image_fillet_delete_iv://图片删除


                appointment_write_image_iv.setImageResource(R.drawable.travel_image);
                filePath = null;
                appointment_write_image_fillet_delete_iv.setVisibility(View.GONE);

                break;
        }
    }


    private void dataDealWith() {

        if (isRepeat) return;

        if (TextUtils.isEmpty(destinationContext)) {
            DialogUtil.showDisCoverNetToast(TravelActivity.this, getString(R.string.text_appointment_hini));
        } else if (null == addSetectsList || addSetectsList.size() <= 0) {
            DialogUtil.showDisCoverNetToast(TravelActivity.this, getString(R.string.text_appointment_hini));
        } else if (TextUtils.isEmpty(travel_layout_objects_tv.getText().toString().trim())) {//目标对象
            DialogUtil.showDisCoverNetToast(TravelActivity.this, getString(R.string.text_appointment_hini));
        } else if (TextUtils.isEmpty(travel_layout_estimated_time_tv.getText().toString().trim())) {//预计时间
            DialogUtil.showDisCoverNetToast(TravelActivity.this, getString(R.string.text_appointment_hini));

        } else if (TextUtils.isEmpty(travel_layout_travel_tv.getText().toString().trim())) {//出行
            DialogUtil.showDisCoverNetToast(TravelActivity.this, getString(R.string.text_appointment_hini));
        } else if (TextUtils.isEmpty(travel_layout_pay_tv.getText().toString().trim())) {//买单
            DialogUtil.showDisCoverNetToast(TravelActivity.this, getString(R.string.text_appointment_hini));
        } else if (TextUtils.isEmpty(mDescriptionContent)) {
            DialogUtil.showDisCoverNetToast(TravelActivity.this, getString(R.string.text_appointment_hini));
        } else {

//           String filePath,String destinationContext,String[] wishTags,
//                   String [] meaningTags,int objectId,int meetingTravelTimeType,
//           String meetingTime,int planTime,int method,int travelCostType,String description


            int preferencesSetectsSize = preferencesSetects.size();// 喜欢与偏好。wishTags  preferencesSetects

            String[] wishTags = new String[preferencesSetectsSize];
            for (int i = 0; i < preferencesSetects.size(); i++) {
                wishTags[i] = preferencesSetects.get(i).getContent();
            }


            int significanceSetectsSize = significanceSetects.size();//意义与标签。meaningTags  significanceSetects

            String[] meaningTags = new String[significanceSetectsSize];
            for (int i = 0; i < significanceSetects.size(); i++) {
                meaningTags[i] = significanceSetects.get(i).getContent();
            }


            int objectId = InfoTransformUtils.getTargetObject(mTravelObjects.getContext());

            int meetingTravelTimeType = InfoTransformUtils.getMeetingTravelTimeType(meetingTravelTimeTypeStr);

            String meetingTime = "";

            if (TextUtils.equals("具体日期", meetingTravelTimeTypeStr)) {
                StringBuffer sb = new StringBuffer();
                sb.append(days);
                sb.append("-");
                sb.append(month);
                sb.append("-");
                sb.append(Date);

                meetingTime = sb.toString();
            }

            int planTime = InfoTransformUtils.getPlanTime(planTimeStr);

            int method = InfoTransformUtils.getMethod(mTravelPay.getTheWay());

            int travelCostType = InfoTransformUtils.getTravelCostType(mTravelPay.getPay());

            isRepeat = true;
            loadingDiaog.show();

            douploadingUserImg(filePath, destinationContext, wishTags, meaningTags, objectId,
                    meetingTravelTimeType, meetingTime, planTime, method, travelCostType, mDescriptionContent);


        }


    }


    class AddTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (!TextUtils.isEmpty(s)) {
                mDescriptionContent = s.toString().trim();

            }


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                mDescriptionContent = s.toString().trim();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(mDescriptionContent)) {

                int length = mDescriptionContent.length();

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(length);

                stringBuilder.append("/200");

                appointment_write_et_number_tv.setText(stringBuilder.toString());


            }
        }
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

                            travel_layout_departure_time_tv.setText(sb.toString());

                        }


                    }


                }
                break;
            case YpSettings.TRAVE_DESTINATION:

                if (requestCode == YpSettings.TRAVE_DESTINATION) {

                    if (data != null) {
                        destinationContext = data.getExtras().getString("content");
                        travel_layout_destination_tv.setText(destinationContext);
                    }

                }
                break;
            case YpSettings.TRAVE_LABEL:

                if (resultCode == YpSettings.TRAVE_LABEL) {

                    if (data != null) {
                        significanceSetects = (List<TravelLabelSetect>) data.getExtras().getSerializable("significanceSetects");
                        preferencesSetects = (List<TravelLabelSetect>) data.getExtras().getSerializable("preferencesSetects");


                        addSetectsList = new ArrayList<>();

                        if (significanceSetects != null && significanceSetects.size() > 0) {
                            addSetectsList.addAll(significanceSetects);
                        }
                        if (preferencesSetects != null && preferencesSetects.size() > 0) {
                            addSetectsList.addAll(preferencesSetects);
                        }


                        if (addSetectsList != null && addSetectsList.size() > 0) {
                            travel_layout_lael_tv.setVisibility(View.GONE);
                            travel_layout_label_lable_flowlayout.setVisibility(View.VISIBLE);
                            initLableViews(travel_layout_label_lable_flowlayout, addSetectsList);
                        } else {
                            travel_layout_lael_tv.setVisibility(View.VISIBLE);
                            travel_layout_label_lable_flowlayout.setVisibility(View.GONE);
                        }


                    }

                }
                break;
            case YpSettings.TRAVE_OBJECTS:

                if (resultCode == YpSettings.TRAVE_OBJECTS) {

                    if (data != null) {
                        mTravelObjects = (TravelObjects) data.getExtras().getSerializable("mTravelObjects");
                        if (null == mTravelObjects) {
                            return;
                        }
                        travel_layout_objects_tv.setText(mTravelObjects.getContext());


                    }

                }
                break;
            case YpSettings.TRAVE_PAY:

                if (resultCode == YpSettings.TRAVE_PAY) {

                    if (data != null) {
                        mTravelPay = (TravelPay) data.getExtras().getSerializable("mTravelPay");
                        if (null == mTravelPay) {
                            return;
                        }

                        if (!TextUtils.isEmpty(mTravelPay.getTheWay())) {
                            travel_layout_travel_tv.setText(mTravelPay.getTheWay());
                        } else {
                            travel_layout_travel_tv.setText("");
                        }
                        if (!TextUtils.isEmpty(mTravelPay.getPay())) {
                            travel_layout_pay_tv.setText(mTravelPay.getPay());
                        } else {
                            travel_layout_pay_tv.setText("");
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


    private void initLableViews(FlowLeftLayout lableLayout, List<TravelLabelSetect> lableList) {
        lableLayout.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 12;
        lp.rightMargin = 12;
        lp.topMargin = 12;
        lp.bottomMargin = 12;

        if (lableList != null && lableList.size() > 0) {

            for (int i = 0; i < lableList.size(); i++) {
                TextView view = new TextView(this);
                view.setText(lableList.get(i).getContent());
                view.setTextSize(15);
                view.setPadding(40, 10, 40, 10);
                view.setGravity(Gravity.CENTER_HORIZONTAL);
                view.setMaxEms(5);
                view.setSingleLine(true);
                view.setEllipsize(TextUtils.TruncateAt.END);
                view.setBackgroundResource(R.drawable.travel_label_lable_corners_default);
                view.setTextColor(getResources().getColor(R.color.color_333333));
                lableLayout.addView(view, lp);


            }
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
                DialogUtil.showDisCoverNetToast(TravelActivity.this, "选取失败，请重新选择上传！");
            }
        } else {
            DialogUtil.showDisCoverNetToast(TravelActivity.this, "选取失败，请重新选择上传！");
        }

    }

    /**
     * 上传图片
     *
     * @param filePath
     */
    private void douploadingUserImg(final String filePath, final String destinationContext, final String[] wishTags,
                                    final String[] meaningTags, final int objectId, final int meetingTravelTimeType,
                                    final String meetingTime, final int planTime, final int method, final int travelCostType, final String description) {

        if (TextUtils.isEmpty(filePath)) {
            postData("", destinationContext, wishTags,
                    meaningTags, objectId, meetingTravelTimeType,
                    meetingTime, planTime, method, travelCostType, description);

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

                postData(url, destinationContext, wishTags,
                        meaningTags, objectId, meetingTravelTimeType,
                        meetingTime, planTime, method, travelCostType, description);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                postData("", destinationContext, wishTags,
                        meaningTags, objectId, meetingTravelTimeType,
                        meetingTime, planTime, method, travelCostType, description);

            }
        });
        datingsImageService.enqueue();

    }

    private void postData(String filePath, String destinationContext, String[] wishTags,
                          String[] meaningTags, int objectId, int meetingTravelTimeType,
                          String meetingTime, int planTime, int method, int travelCostType, String description) {

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

        datingsBean.setActivityType(Constant.APPOINT_TYPE_TRAVEL);


        Travel travel = new Travel();

        travel.setWishTags(wishTags);

        travel.setTargetObject(objectId);

        travel.setPlanTime(planTime);

        travel.setMethod(method);

        travel.setTravelCostType(travelCostType);

        travel.setPhotoUrl(filePath);

        travel.setMeaningTags(meaningTags);

        travel.setMeetingTime(meetingTime);

        travel.setMeetingTravelTimeType(meetingTravelTimeType);

        travel.setDescription(description);


        if (!TextUtils.equals(getString(R.string.discuss_decide), destinationContext) && !TextUtils.equals(getString(R.string.where_to_go), destinationContext)) {
            travel.setAddress(destinationContext);
        }


        datingsBean.setTravel(travel);

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

                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(2));

                    } else if (TextUtils.equals("AppointmentFragment", frompage)) {

                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(1));
                    }

                    finish();

                    return;
                }


                String limitMsg = resp.getLimitMsg();

                if (!TextUtils.isEmpty(limitMsg)) {


                    canMsgNotPublishDatingDialog = DialogUtil.createHintOperateDialog(TravelActivity.this, "", limitMsg, "", "确定", new BackCallListener() {

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
                        canNotPublishDatingDialog = DialogUtil.createNotCanPublishDatingHintDialog(TravelActivity.this, list, "前往查看", new BackCallListener() {
                            @Override
                            public void onEnsure(View view, Object... obj) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
                                ActivityUtil.jump(TravelActivity.this, UserInfoActivity.class, bundle, 0, 100);
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
                    DialogUtil.showDisCoverNetToast(TravelActivity.this);

                } else {
                    DialogUtil.showDisCoverNetToast(TravelActivity.this, msg);
                }

            }
        });

        datingsService.enqueue();


    }

}
