package cn.chono.yopper.activity.order;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderEvaluation.OrderEvaluationBean;
import cn.chono.yopper.Service.Http.OrderEvaluation.OrderEvaluationEntity;
import cn.chono.yopper.Service.Http.OrderEvaluation.OrderEvaluationRespEntity;
import cn.chono.yopper.Service.Http.OrderEvaluation.OrderEvaluationService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LabelSetectEntity;
import cn.chono.yopper.event.OrderListEvent;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.view.FlowLeftLayout;

/**
 * 评价
 * Created by cc on 16/5/4.
 */
public class UserOrderEvaluationActivity extends MainFrameActivity {

    RatingBar userOrderEvaluation_rbar_score;

    FlowLeftLayout userOrderEvaluation_fll_laber;

    EditText userOrderEvaluation_et_evaluation;

    Button userOrderEvaluation_btn_confirm_submit;

    TextView userOrderEvaluation_et_number_tv;

    List<LabelSetectEntity> mSetectEntityList = new ArrayList<>();

    int mEvaluationFraction;

    String orderId;

    String mEvaluationContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_user_order_evaluation);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.ORDER_ID))
            orderId = bundle.getString(YpSettings.ORDER_ID);

        initView();

    }

    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("订单评价"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("订单评价"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    private void initView() {

        getTvTitle().setText(this.getResources().getString(R.string.evaluate_text));

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userOrderEvaluation_rbar_score = (RatingBar) findViewById(R.id.userOrderEvaluation_rbar_score);

        userOrderEvaluation_fll_laber = (FlowLeftLayout) findViewById(R.id.userOrderEvaluation_fll_laber);

        userOrderEvaluation_et_evaluation = (EditText) findViewById(R.id.userOrderEvaluation_et_evaluation);

        userOrderEvaluation_btn_confirm_submit = (Button) findViewById(R.id.userOrderEvaluation_btn_confirm_submit);

        userOrderEvaluation_et_number_tv = (TextView) findViewById(R.id.userOrderEvaluation_et_number_tv);


        userOrderEvaluation_et_evaluation.addTextChangedListener(new AddTextWatcher());


        Resources res = getResources();
        String[] orderEvaluation = res.getStringArray(R.array.order_evaluation);
        initLableSignificanceViews(userOrderEvaluation_fll_laber, orderEvaluation);

        userOrderEvaluation_rbar_score.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mEvaluationFraction = (int) rating;
            }
        });

        userOrderEvaluation_btn_confirm_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEvaluationFraction == 0) {
                    DialogUtil.showDisCoverNetToast(UserOrderEvaluationActivity.this, UserOrderEvaluationActivity.this.getResources().getString(R.string.fraction_hint));

                    return;
                }


                if (TextUtils.isEmpty(mEvaluationContent)) {

                    DialogUtil.showDisCoverNetToast(UserOrderEvaluationActivity.this, UserOrderEvaluationActivity.this.getResources().getString(R.string.fill_in_hint));
                    return;
                }

                if (mEvaluationContent.length() < 5) {

                    DialogUtil.showDisCoverNetToast(UserOrderEvaluationActivity.this, UserOrderEvaluationActivity.this.getResources().getString(R.string.fill_in_lenght_hint));
                    return;
                }


                int lenght = mSetectEntityList.size();

                String[] tags = new String[lenght];

                for (int i = 0; i < lenght; i++) {
                    tags[i] = mSetectEntityList.get(i).content;
                }


                postEvaluationContent(orderId, mEvaluationFraction, tags, mEvaluationContent);
            }
        });


    }


    class AddTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (!TextUtils.isEmpty(s)) {
                mEvaluationContent = s.toString().trim();

            } else {
                mEvaluationContent = "";
            }


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                mEvaluationContent = s.toString().trim();

            } else {
                mEvaluationContent = "";
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(mEvaluationContent)) {

                int length = mEvaluationContent.length();

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(length);

                stringBuilder.append("/200");

                userOrderEvaluation_et_number_tv.setText(stringBuilder.toString());


            }
        }
    }


    private void postEvaluationContent(String orderId, int stars, String[] tags, String description) {

        OrderEvaluationBean orderEvaluationBean = new OrderEvaluationBean();

        orderEvaluationBean.id = orderId;

        orderEvaluationBean.stars = stars;

        orderEvaluationBean.tags = tags;

        orderEvaluationBean.description = description;

        OrderEvaluationService orderEvaluationService = new OrderEvaluationService(this);


        orderEvaluationService.parameter(orderEvaluationBean);

        orderEvaluationService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);


                OrderEvaluationRespEntity orderEvaluationRespEntity = (OrderEvaluationRespEntity) respBean;

                OrderEvaluationEntity orderEvaluationEntity = orderEvaluationRespEntity.resp;

                if (orderEvaluationEntity == null) {

                    return;


                }

                if (orderEvaluationEntity.result != 0) {

                    DialogUtil.showDisCoverNetToast(UserOrderEvaluationActivity.this, orderEvaluationEntity.msg);

                    return;
                }



                DialogUtil.showDisCoverNetToast(UserOrderEvaluationActivity.this, "提交成功");

                RxBus.get().post("OrderListEvent",new OrderListEvent());


                finish();


            }
        });

        orderEvaluationService.enqueue();


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

                if (mSetectEntityList != null && mSetectEntityList.size() > 0) {
                    for (int j = 0; j < mSetectEntityList.size(); j++) {
                        if (context.equals(mSetectEntityList.get(j).content)) {

                            ishave = true;

                            break;
                        }
                    }
                }


                if (ishave) {
                    view.setBackgroundResource(R.drawable.evaluation_label_lable_corners_setect);
                    view.setTextColor(getResources().getColor(R.color.color_ffffff));
                    view.setTag(new LabelSetectEntity(context, true));
                } else {
                    view.setBackgroundResource(R.drawable.evaluation_label_lable_corners_default);
                    view.setTextColor(getResources().getColor(R.color.color_b2b2b2));
                    view.setTag(new LabelSetectEntity(context, false));
                }


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LabelSetectEntity labelSetectEntity = (LabelSetectEntity) v.getTag();


                        if (labelSetectEntity.isSelect) {

                            v.setTag(new LabelSetectEntity(labelSetectEntity.content, false));
                            v.setBackgroundResource(R.drawable.evaluation_label_lable_corners_default);
                            TextView tvview = (TextView) v;
                            tvview.setTextColor(getResources().getColor(R.color.color_b2b2b2));
                            significanceChangeSelectedListData(new LabelSetectEntity(labelSetectEntity.content, false));

                            return;
                        }

                        v.setTag(new LabelSetectEntity(labelSetectEntity.content, true));
                        v.setBackgroundResource(R.drawable.evaluation_label_lable_corners_setect);
                        TextView tvview = (TextView) v;
                        tvview.setTextColor(getResources().getColor(R.color.color_ffffff));
                        significanceChangeSelectedListData(new LabelSetectEntity(labelSetectEntity.content, true));
                    }
                });


                lableView.addView(view, lp);
            }
        }
    }

    private void significanceChangeSelectedListData(LabelSetectEntity labelSetectEntity) {


        if (labelSetectEntity.isSelect) {
            mSetectEntityList.add(labelSetectEntity);
        } else {
            for (int i = 0; i < mSetectEntityList.size(); i++) {
                if (TextUtils.equals(labelSetectEntity.content, mSetectEntityList.get(i).content)) {
                    mSetectEntityList.remove(i);
                    break;
                }
            }

        }
    }

}
