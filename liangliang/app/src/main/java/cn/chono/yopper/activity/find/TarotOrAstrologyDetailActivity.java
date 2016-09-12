package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileEntity;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileRespEntity;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileService;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorsProfileBean;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationDataEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationListBean;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationListRespEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationTagsEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationsEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationsListService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDateEntity;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDatetimesBean;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDatetimesEntity;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkTimesService;
import cn.chono.yopper.activity.base.BigPhotoActivity;
import cn.chono.yopper.adapter.TarotAstrologyDetailEvaluationAdapter;
import cn.chono.yopper.adapter.TarotAstrologyDetailPhotoAdapter;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.OnAdapterIconClickLitener;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.view.FlowLeftLayout;
import cn.chono.yopper.view.MyGridView;

/**
 * Created by cc on 16/4/26.
 */
public class TarotOrAstrologyDetailActivity extends MainFrameActivity implements OnAdapterItemClickLitener, OnAdapterIconClickLitener {

    ImageView tarotOrAstrologyDetail_iv_back, tarotOrAstrologyDetail_iv_icon;

    TextView tarotOrAstrologyDetail_tv_name;

    ImageView tarotOrAstrologyDetail_iv_sex;

    TextView tarotOrAstrologyDetail_tv_answer_problem, tarotOrAstrologyDetail_tv_price, tarotOrAstrologyDetail_tv_price_unit;

    LinearLayout tarotOrAstrologyDetail_ll_basic_info, tarotOrAstrologyDetail_ll_basic_info_content;

    TextView tarotOrAstrologyDetail_tv_practitioners_time, tarotOrAstrologyDetail_tv_basic_info_content;

    LinearLayout tarotOrAstrologyDetail_ll_label;

    LinearLayout tarotOrAstrologyDetail_ll_stores_info, tarotOrAstrologyDetail_ll_stores_info_content;

    TextView tarotOrAstrologyDetail_tv_stores_info_address;

    LinearLayout tarotOrAstrologyDetail_ll_photo, tarotOrAstrologyDetail_ll_photo_content, tarotOrAstrologyDetail_ll_evaluation;

    MyGridView tarotOrAstrologyDetail_gv_photo;

    TextView tarotOrAstrologyDetail_tv_photo_more;

    TextView tarotOrAstrologyDetail_tv_evaluation;

    ListView tarotOrAstrologyDetail_lv_evaluation;

    Button tarotOrAstrologyDetail_btn_straightway_advisory;

    TextView tarotOrAstrologyDetail_btn_reservation;

    RatingBar tarotOrAstrologyDetail_rbar_evaluation;

    TextView tarotOrAstrologyDetail_tv_evaluation_number;

    FlowLeftLayout tarotOrAstrologyDetail_fll_evaluation_label;

    TarotAstrologyDetailPhotoAdapter mTarotAstrologyDetailPhotoAdapter;

    TarotAstrologyDetailEvaluationAdapter mTarotAstrologyDetailEvaluationAdapter;


    int userId;

    int mCounselorType;

    String[] mAlbumImages;

    Dialog reservation_dlg, mLoadingDiaog;

    ViewStub tarotOrAstrologyDetail_vs_no_data;

    LinearLayout tarotOrAstrologyDetail_ll_content;

    TextView error_no_data_tv;

    String mUserName;

    long mCharge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tarot_astrology_detail);
        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.USERID))
            userId = bundle.getInt(YpSettings.USERID);

        if (bundle.containsKey(YpSettings.COUNSEL_TYPE))
            mCounselorType = bundle.getInt(YpSettings.COUNSEL_TYPE);

        initView();

        NoData(View.GONE);

        tarotOrAstrologyDetail_ll_content.setVisibility(View.GONE);

        getTarotOrAstrologyEvaluationList();

        initData();


    }

    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("塔罗占星详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("塔罗占星详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    private void initView() {

        mLoadingDiaog = DialogUtil.LoadingDialog(this);

        tarotOrAstrologyDetail_vs_no_data = (ViewStub) findViewById(R.id.tarotOrAstrologyDetail_vs_no_data);


        tarotOrAstrologyDetail_ll_content = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_content);

        tarotOrAstrologyDetail_iv_back = (ImageView) findViewById(R.id.tarotOrAstrologyDetail_iv_back);
        tarotOrAstrologyDetail_iv_icon = (ImageView) findViewById(R.id.tarotOrAstrologyDetail_iv_icon);

        int width = UnitUtil.getScreenWidthPixels(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);

        tarotOrAstrologyDetail_iv_icon.setLayoutParams(params);






        tarotOrAstrologyDetail_tv_name = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_name);
        tarotOrAstrologyDetail_iv_sex = (ImageView) findViewById(R.id.tarotOrAstrologyDetail_iv_sex);
        tarotOrAstrologyDetail_tv_answer_problem = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_answer_problem);
        tarotOrAstrologyDetail_tv_price = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_price);
        tarotOrAstrologyDetail_tv_price_unit = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_price_unit);
        tarotOrAstrologyDetail_ll_basic_info = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_basic_info);
        tarotOrAstrologyDetail_ll_basic_info_content = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_basic_info_content);
        tarotOrAstrologyDetail_tv_practitioners_time = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_practitioners_time);
        tarotOrAstrologyDetail_tv_basic_info_content = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_basic_info_content);
        tarotOrAstrologyDetail_ll_label = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_label);
        tarotOrAstrologyDetail_ll_stores_info = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_stores_info);
        tarotOrAstrologyDetail_ll_stores_info_content = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_stores_info_content);
        tarotOrAstrologyDetail_tv_stores_info_address = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_stores_info_address);
        tarotOrAstrologyDetail_ll_photo = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_photo);
        tarotOrAstrologyDetail_ll_photo_content = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_photo_content);
        tarotOrAstrologyDetail_ll_evaluation = (LinearLayout) findViewById(R.id.tarotOrAstrologyDetail_ll_evaluation);
        tarotOrAstrologyDetail_gv_photo = (MyGridView) findViewById(R.id.tarotOrAstrologyDetail_gv_photo);

        tarotOrAstrologyDetail_tv_photo_more = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_photo_more);
        tarotOrAstrologyDetail_tv_evaluation = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_evaluation);
        tarotOrAstrologyDetail_lv_evaluation = (ListView) findViewById(R.id.tarotOrAstrologyDetail_lv_evaluation);
        tarotOrAstrologyDetail_btn_straightway_advisory = (Button) findViewById(R.id.tarotOrAstrologyDetail_btn_straightway_advisory);
        tarotOrAstrologyDetail_btn_reservation = (TextView) findViewById(R.id.tarotOrAstrologyDetail_btn_reservation);
        tarotOrAstrologyDetail_rbar_evaluation = (RatingBar) findViewById(R.id.tarotOrAstrologyDetail_rbar_evaluation);
        tarotOrAstrologyDetail_tv_evaluation_number = (TextView) findViewById(R.id.tarotOrAstrologyDetail_tv_evaluation_number);
        tarotOrAstrologyDetail_fll_evaluation_label = (FlowLeftLayout) findViewById(R.id.tarotOrAstrologyDetail_fll_evaluation_label);

        tarotOrAstrologyDetail_btn_reservation.setEnabled(false);
        tarotOrAstrologyDetail_btn_reservation.setText(getResources().getString(R.string.reservation_full));


        mTarotAstrologyDetailPhotoAdapter = new TarotAstrologyDetailPhotoAdapter(this);

        mTarotAstrologyDetailPhotoAdapter.setOnAdapterItemClickLitener(this);

        tarotOrAstrologyDetail_gv_photo.setAdapter(mTarotAstrologyDetailPhotoAdapter);


        mTarotAstrologyDetailEvaluationAdapter = new TarotAstrologyDetailEvaluationAdapter(this);

        mTarotAstrologyDetailEvaluationAdapter.setOnAdapterIconClickLitener(this);


        tarotOrAstrologyDetail_lv_evaluation.setAdapter(mTarotAstrologyDetailEvaluationAdapter);


        tarotOrAstrologyDetail_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tarotOrAstrologyDetail_tv_photo_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putStringArray(YpSettings.PHOTO_STRING, mAlbumImages);


                ActivityUtil.jump(TarotOrAstrologyDetailActivity.this, TarotOrAstrologyPhotoActivity.class, bundle, 0, 100);

            }
        });


        tarotOrAstrologyDetail_tv_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.USERID, userId);
                bundle.putInt(YpSettings.COUNSEL_TYPE, mCounselorType);

                ActivityUtil.jump(TarotOrAstrologyDetailActivity.this, TarotOrAstrologyEvaluationListActivity.class, bundle, 0, 100);
            }
        });

        tarotOrAstrologyDetail_btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mCounselorType == Constant.CounselorType_Tarot) {

                    MobclickAgent.onEvent(TarotOrAstrologyDetailActivity.this, "btn_find_event_Tarot_reservation");

                } else if (mCounselorType == Constant.CounselorType_Astrology) {

                    MobclickAgent.onEvent(TarotOrAstrologyDetailActivity.this, "btn_find_event_Astrolabe_reservation");

                }
                getWorkDateTimesData(userId, (Integer) v.getTag());


            }
        });

        tarotOrAstrologyDetail_ll_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.USERID, userId);
                bundle.putInt(YpSettings.COUNSEL_TYPE, mCounselorType);

                ActivityUtil.jump(TarotOrAstrologyDetailActivity.this, TarotOrAstrologyEvaluationListActivity.class, bundle, 0, 100);
            }
        });

        mTarotAstrologyDetailEvaluationAdapter.setOnAdapterItemClickLitener(new OnAdapterItemClickLitener() {
            @Override
            public void onAdapterItemClick(int position, Object data) {
                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.USERID, userId);
                bundle.putInt(YpSettings.COUNSEL_TYPE, mCounselorType);

                ActivityUtil.jump(TarotOrAstrologyDetailActivity.this, TarotOrAstrologyEvaluationListActivity.class, bundle, 0, 100);
            }
        });


    }


    private void NoData(int Visibility) {
        if (Visibility == View.GONE) {
            tarotOrAstrologyDetail_vs_no_data.setVisibility(View.GONE);
        } else {
            tarotOrAstrologyDetail_vs_no_data.setVisibility(View.VISIBLE);

            error_no_data_tv = (TextView) findViewById(R.id.error_no_data_tv);
            error_no_data_tv.setText("暂无数据");
        }

    }

    /**
     * 初始化数据。获取详情请求
     */
    private void initData() {

        mLoadingDiaog.show();

        CounselorsProfileBean counselorsProfileBean = new CounselorsProfileBean();

        counselorsProfileBean.userId = userId;

        counselorsProfileBean.counselorType = mCounselorType;

        CounselorProfileService counselorProfileService = new CounselorProfileService(this);

        counselorProfileService.parameter(counselorsProfileBean);

        counselorProfileService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                LogUtils.e(respBean.toString());

                mLoadingDiaog.dismiss();

                CounselorProfileRespEntity counselorProfileRespEntity = (CounselorProfileRespEntity) respBean;

                CounselorProfileEntity counselorProfileEntity = counselorProfileRespEntity.resp;

                if (counselorProfileEntity == null) {

                    tarotOrAstrologyDetail_ll_content.setVisibility(View.GONE);
                    NoData(View.VISIBLE);

                    return;
                }

                NoData(View.GONE);

                tarotOrAstrologyDetail_ll_content.setVisibility(View.VISIBLE);


                String iconUrl = ImgUtils.DealImageUrl(counselorProfileEntity.avatar,640,640);

                Glide.with(TarotOrAstrologyDetailActivity.this).load(iconUrl).centerCrop().into(tarotOrAstrologyDetail_iv_icon);

                mUserName = counselorProfileEntity.nickName;

                tarotOrAstrologyDetail_tv_name.setText(mUserName);


                if (counselorProfileEntity.sex == 1) {//男

                    tarotOrAstrologyDetail_iv_sex.setImageResource(R.drawable.tarot_astrology_sex_male);


                } else if (counselorProfileEntity.sex == 2) {//女

                    tarotOrAstrologyDetail_iv_sex.setImageResource(R.drawable.tarot_astrology_sex_female);

                } else {
                    tarotOrAstrologyDetail_iv_sex.setVisibility(View.GONE);
                }

                if (counselorProfileEntity.isFullReservation) {
                    tarotOrAstrologyDetail_btn_reservation.setText(getResources().getString(R.string.reservation_full));
                    tarotOrAstrologyDetail_btn_reservation.setEnabled(false);
                } else {
                    tarotOrAstrologyDetail_btn_reservation.setText(getResources().getString(R.string.reservation));
                    tarotOrAstrologyDetail_btn_reservation.setEnabled(true);
                }


                tarotOrAstrologyDetail_tv_answer_problem.setText(counselorProfileEntity.totalAnswerCount + "");

                mCharge = counselorProfileEntity.serviceTypePrice.charge / 100;

                StringBuilder sb = new StringBuilder();

                sb.append("￥");
                sb.append(mCharge);

                tarotOrAstrologyDetail_tv_price.setText(sb.toString());


                if (mCounselorType == Constant.CounselorType_Tarot) {
                    tarotOrAstrologyDetail_tv_price_unit.setText("问题");
                } else if (mCounselorType == Constant.CounselorType_Astrology) {
                    tarotOrAstrologyDetail_tv_price_unit.setText("星盘");
                } else if (mCounselorType == Constant.CounselorType_Psychological) {//心理咨询师

                }


                tarotOrAstrologyDetail_btn_reservation.setTag(counselorProfileEntity.serviceTypePrice.counselorType);


                StringBuilder timesb = new StringBuilder();

                timesb.append("从业时间：");
                timesb.append(counselorProfileEntity.serviceTypePrice.workTimeDesc);

                tarotOrAstrologyDetail_tv_practitioners_time.setText(timesb.toString());

                if (!TextUtils.isEmpty(counselorProfileEntity.personalDesc)) {
                    tarotOrAstrologyDetail_tv_basic_info_content.setText(counselorProfileEntity.personalDesc);
                }


                setTarotAstrologrLabel(tarotOrAstrologyDetail_ll_label, counselorProfileEntity.skillTags);

                if (TextUtils.isEmpty(counselorProfileEntity.storeAddress)) {
                    tarotOrAstrologyDetail_ll_stores_info.setVisibility(View.GONE);
                    tarotOrAstrologyDetail_ll_stores_info_content.setVisibility(View.GONE);
                } else {
                    tarotOrAstrologyDetail_tv_stores_info_address.setText(counselorProfileEntity.storeAddress);
                }


                if (counselorProfileEntity.albumImages != null && counselorProfileEntity.albumImages.length > 0) {

                    mAlbumImages = counselorProfileEntity.albumImages;

                    if (counselorProfileEntity.albumImages.length <= 4) {
                        tarotOrAstrologyDetail_tv_photo_more.setVisibility(View.GONE);

                    } else {
                        tarotOrAstrologyDetail_tv_photo_more.setVisibility(View.VISIBLE);
                    }

                    mTarotAstrologyDetailPhotoAdapter.setData(counselorProfileEntity.albumImages);
                } else {
                    tarotOrAstrologyDetail_ll_photo.setVisibility(View.GONE);
                    tarotOrAstrologyDetail_ll_photo_content.setVisibility(View.GONE);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                LogUtils.e(respBean.toString());

                mLoadingDiaog.dismiss();

                if (TextUtils.equals("400", respBean.getStatus())) {


                    reservation_dlg = DialogUtil.createHintOperateDialog(TarotOrAstrologyDetailActivity.this, "", respBean.msg, "", "好的", new BackCallListener() {
                        @Override
                        public void onCancel(View view, Object... obj) {

                            reservation_dlg.dismiss();

                        }

                        @Override
                        public void onEnsure(View view, Object... obj) {

                            reservation_dlg.dismiss();
                            finish();


                        }
                    });
                    reservation_dlg.show();

                    return;

                }

                NoData(View.VISIBLE);
                tarotOrAstrologyDetail_ll_content.setVisibility(View.GONE);


            }
        });
        counselorProfileService.enqueue();

    }

    /**
     * 获取评价内容
     */
    private void getTarotOrAstrologyEvaluationList() {


        EvaluationListBean evaluationListBean = new EvaluationListBean();

        evaluationListBean.receiveUserId = userId;
        evaluationListBean.start = 0;
        evaluationListBean.rows = 3;

        EvaluationsListService evaluationsListService = new EvaluationsListService(this);


        evaluationsListService.parameter(evaluationListBean);

        evaluationsListService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                EvaluationListRespEntity evaluationListRespEntity = (EvaluationListRespEntity) respBean;

                EvaluationDataEntity evaluationDataEntity = evaluationListRespEntity.resp;


                if (evaluationDataEntity == null) {

                    tarotOrAstrologyDetail_ll_evaluation.setVisibility(View.GONE);

                    return;
                }

                EvaluationsEntity evaluationsEntity = evaluationDataEntity.evaluations;


                if (evaluationsEntity == null) {

                    tarotOrAstrologyDetail_ll_evaluation.setVisibility(View.GONE);

                    return;

                }

                if (evaluationsEntity.list == null || evaluationsEntity.list.size() == 0) {

                    tarotOrAstrologyDetail_ll_evaluation.setVisibility(View.GONE);

                    return;
                }

                tarotOrAstrologyDetail_ll_evaluation.setVisibility(View.VISIBLE);

                StringBuilder sb = new StringBuilder();

                sb.append("用户评价");
                sb.append("（");
                sb.append(evaluationDataEntity.evaluationsAggregation.total);
                sb.append("）");

                tarotOrAstrologyDetail_tv_evaluation.setText(sb.toString());

                tarotOrAstrologyDetail_rbar_evaluation.setRating((float) evaluationDataEntity.evaluationsAggregation.avgStars);

                tarotOrAstrologyDetail_tv_evaluation_number.setText(evaluationDataEntity.evaluationsAggregation.avgStars + "");


                setTarotAstrologrEvaluationLabel(tarotOrAstrologyDetail_fll_evaluation_label, evaluationDataEntity.evaluationsAggregation.tags);

                mTarotAstrologyDetailEvaluationAdapter.setData(evaluationsEntity.list);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                tarotOrAstrologyDetail_ll_evaluation.setVisibility(View.GONE);


            }
        });

        evaluationsListService.enqueue();

    }

    /**
     * 设置标签
     *
     * @param linearLayout
     * @param data
     */
    private void setTarotAstrologrLabel(LinearLayout linearLayout, String[] data) {

        linearLayout.removeAllViews();

        if (null == data || data.length < 1) {
            return;
        }

        for (int i = 0; i < data.length; i++) {
            if (i > 4) break;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 0, 12, 0);


            TextView textView = new TextView(this);

            textView.setTextSize(12);

            textView.setText(data[i]);


            textView.setMinWidth(45);

            textView.setGravity(Gravity.CENTER);

            textView.setPadding(30, 10, 30, 10);

            textView.setTextColor(this.getResources().getColor(R.color.color_b2b2b2));

            textView.setBackgroundResource(R.color.color_f5f5f5);

            textView.setLayoutParams(params);

            linearLayout.addView(textView);

        }


    }

    /**
     * 设置评价标签
     *
     * @param lableLayout
     * @param tags
     */
    private void setTarotAstrologrEvaluationLabel(FlowLeftLayout lableLayout, List<EvaluationTagsEntity> tags) {
        lableLayout.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 0;
        lp.rightMargin = 15;
        lp.topMargin = 15;
        lp.bottomMargin = 15;

        if (tags != null && tags.size() > 0) {

            for (int i = 0; i < tags.size(); i++) {

                String key = tags.get(i).key;

                int vaule = tags.get(i).value;

                StringBuilder sb = new StringBuilder();

                sb.append(key);
                sb.append("(");
                sb.append(vaule);
                sb.append(")");


                TextView view = new TextView(TarotOrAstrologyDetailActivity.this);
                view.setText(sb.toString());
                view.setTextSize(12);
                view.setPadding(40, 10, 40, 10);

                view.setGravity(Gravity.CENTER_HORIZONTAL);

                view.setBackgroundResource(R.color.color_20b2b2b2);

                view.setTextColor(TarotOrAstrologyDetailActivity.this.getResources().getColor(R.color.color_b2b2b2));


                lableLayout.addView(view, lp);
            }
        }
    }


    /**
     * 获取预约时间
     */
    private void getWorkDateTimesData(final int userId, int counselorType) {


        WorkDatetimesBean workDatetimesBean = new WorkDatetimesBean();

        workDatetimesBean.userId = userId;

        workDatetimesBean.counselorType = counselorType;


        WorkTimesService workTimesService = new WorkTimesService(this);

        workTimesService.parameter(workDatetimesBean);

        workTimesService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                WorkDatetimesEntity workDatetimesEntity = (WorkDatetimesEntity) respBean;

                WorkDateEntity workDateEntity = workDatetimesEntity.resp;

                if (workDateEntity == null)
                    return;


                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.USERID, userId);
                bundle.putString(YpSettings.USER_NAME, mUserName);
                bundle.putLong(YpSettings.COUNSE_CHARE, mCharge);
                bundle.putInt(YpSettings.COUNSEL_TYPE, mCounselorType);
                bundle.putParcelable(YpSettings.COUNSE_DATA, workDateEntity);

                ActivityUtil.jump(TarotOrAstrologyDetailActivity.this, TarotOrAstrologyTimeActivity.class, bundle, 0, 100);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                if (TextUtils.equals("400", respBean.getStatus())) {


                    reservation_dlg = DialogUtil.createHintOperateDialog(TarotOrAstrologyDetailActivity.this, "", respBean.msg, "", "好的", new BackCallListener() {
                        @Override
                        public void onCancel(View view, Object... obj) {

                            reservation_dlg.dismiss();

                        }

                        @Override
                        public void onEnsure(View view, Object... obj) {

                            reservation_dlg.dismiss();
//                            finish();


                        }
                    });
                    reservation_dlg.show();

                    return;


                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(TarotOrAstrologyDetailActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(TarotOrAstrologyDetailActivity.this, msg);

            }
        });

        workTimesService.enqueue();


    }


    @Override
    public void onAdapterItemClick(int position, Object data) {

        Bundle bundle = new Bundle();

        bundle.putStringArray(YpSettings.PHOTO_STRING, (String[]) data);

        bundle.putInt(YpSettings.PHOTO_SUBSCRIPT, position);


        ActivityUtil.jump(TarotOrAstrologyDetailActivity.this, BigPhotoActivity.class, bundle, 0, 100);
    }

    @Override
    public void onAdapterIconClick(int position, Object data) {
        int userId = (int) data;
        Bundle userbundle = new Bundle();
        userbundle.putInt(YpSettings.USERID, userId);
        ActivityUtil.jump(this, UserInfoActivity.class, userbundle, 0, 100);
    }
}
