package cn.chono.yopper.activity.order;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.event.AppleListEvent;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.GameOrderEvent;
import cn.chono.yopper.event.OrderListEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 订单支付成功页面
 */
public class UserOrderPaySuccessActivity extends MainFrameActivity {


    private ViewStub order_pay_success_consult_vs;

    private ViewStub order_pay_success_p_vs;

    private ViewStub order_pay_success_activity_vs;

    private int orderType = 0;// ，1是买苹果，2是游戏星钻,3是活动支付

    private String orderNo;
    private String name;
    private String time;

    private String  productName;

    private int counselType; // 咨询类型（0：塔罗 1：星盘）

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_order_pay_success);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey(YpSettings.ORDER_TYPE)) {
                orderType = bundle.getInt(YpSettings.ORDER_TYPE);
            }


            if (bundle.containsKey(YpSettings.ProductName)) {
                productName = bundle.getString(YpSettings.ProductName);
            }

            if (bundle.containsKey(YpSettings.ORDER_NO)) {
                orderNo = bundle.getString(YpSettings.ORDER_NO);
            }

            if (bundle.containsKey(YpSettings.CONSULT_NAME)) {
                name = bundle.getString(YpSettings.CONSULT_NAME);
            }

            if (bundle.containsKey(YpSettings.CONSULT_TIME)) {
                time = bundle.getString(YpSettings.CONSULT_TIME);
            }

            if (bundle.containsKey(YpSettings.COUNSEL_TYPE)) {
                counselType = bundle.getInt(YpSettings.COUNSEL_TYPE);
            }
        }

        LogUtils.e("orderType=" + orderType);
        LogUtils.e("APPLE_NUM=" + orderNo);
        LogUtils.e("ORDER_NO=" + name);
        LogUtils.e("time=" + time);
        LogUtils.e("counselType=" + counselType);

        initView();

        if (orderType == 0) {

            setConSultViewData();

        } else if (orderType == 1) {

            setAppleAndGameViewData();
        } else if (orderType == 2) {

            setAppleAndGameViewData();

        } else if (orderType == 3) {

            setActivityViewData();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("支付结果"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("支付结果"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    private void initView() {

        // 设置标题栏
        this.getTvTitle().setText("支付结果");
        this.getBtnGoBack().setVisibility(View.VISIBLE);

        if (orderType == 0) {

            this.getOptionLayout().setVisibility(View.VISIBLE);
            this.getBtnOption().setVisibility(View.GONE);
            this.gettvOption().setText("完成");
            this.gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));

        } else {
            this.getOptionLayout().setVisibility(View.INVISIBLE);
        }

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });

        this.getOptionLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });

        order_pay_success_consult_vs = (ViewStub) findViewById(R.id.order_pay_success_consult_vs);

        order_pay_success_p_vs = (ViewStub) findViewById(R.id.order_pay_success_p_vs);

        order_pay_success_activity_vs = (ViewStub) findViewById(R.id.order_pay_success_activity_vs);
    }

    @Override
    public void finish() {


        if (orderType == 0) {

            RxBus.get().post("OrderListEvent", new OrderListEvent());

        } else if (orderType == 1) {

            RxBus.get().post("AppleListEvent", new AppleListEvent());

            RxBus.get().post("AppleBuySuccess", new CommonEvent<>());

        } else if (orderType == 2) {

            RxBus.get().post("GameOrderEvent",new GameOrderEvent());

        }

        super.finish();


    }

    private void setConSultViewData() {

        order_pay_success_consult_vs.setVisibility(View.VISIBLE);
        order_pay_success_p_vs.setVisibility(View.GONE);
        order_pay_success_activity_vs.setVisibility(View.GONE);
        TextView order_pay_success_consult_orderId_tv = (TextView) findViewById(R.id.order_pay_success_consult_orderId_tv);
        TextView order_pay_success_consult_name_tv = (TextView) findViewById(R.id.order_pay_success_consult_name_tv);
        TextView order_pay_success_consult_time_tv = (TextView) findViewById(R.id.order_pay_success_consult_time_tv);

        TextView order_pay_success_consult_tv_name_title = (TextView) findViewById(R.id.order_pay_success_consult_tv_name_title);

        if (counselType == Constant.CounselorType_Tarot) {

            order_pay_success_consult_tv_name_title.setText("塔  罗  师");

        } else if (counselType == Constant.CounselorType_Astrology) {

            order_pay_success_consult_tv_name_title.setText("占  星  师");
        }

        order_pay_success_consult_orderId_tv.setText(":   " + orderNo);

        order_pay_success_consult_name_tv.setText(":   " + name);

        order_pay_success_consult_time_tv.setText(":   " + time);
    }

    private void setAppleAndGameViewData() {

        order_pay_success_consult_vs.setVisibility(View.GONE);
        order_pay_success_p_vs.setVisibility(View.VISIBLE);
        order_pay_success_activity_vs.setVisibility(View.GONE);

        ImageView order_pay_success_iv = (ImageView) findViewById(R.id.order_pay_success_iv);

        TextView order_pay_success_p_num_tv = (TextView) findViewById(R.id.order_pay_success_p_num_tv);
        TextView order_pay_success_game_sure_tv = (TextView) findViewById(R.id.order_pay_success_game_sure_tv);

        if (orderType == 1) {

            order_pay_success_iv.setBackgroundResource(R.drawable.ic_apple);
            order_pay_success_p_num_tv.setText("已成功购买"+productName);

        } else {

            order_pay_success_iv.setBackgroundResource(R.drawable.ic_starjewel_result);
            order_pay_success_p_num_tv.setText("已成功购买"+productName);

        }


        order_pay_success_game_sure_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderType == 2) {
                 RxBus.get().post("GameOrderEvent",new GameOrderEvent());
                }
                finish();
            }
        });
    }

    void setActivityViewData() {
        order_pay_success_consult_vs.setVisibility(View.GONE);
        order_pay_success_p_vs.setVisibility(View.GONE);
        order_pay_success_activity_vs.setVisibility(View.VISIBLE);
        TextView order_pay_success_consult_orderId_tv = (TextView) findViewById(R.id.order_pay_success_consult_orderId_tv);
        TextView order_pay_success_consult_name_tv = (TextView) findViewById(R.id.order_pay_success_consult_name_tv);
        TextView order_pay_success_consult_time_tv = (TextView) findViewById(R.id.order_pay_success_consult_time_tv);
        TextView order_pay_success_tuikuan_tv = (TextView) findViewById(R.id.tuikuan);
        order_pay_success_tuikuan_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "退款详情");
                bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "help/Refund");
                ActivityUtil.jump(UserOrderPaySuccessActivity.this, SimpleWebViewActivity.class, bundle, 0, 0);
            }
        });
        order_pay_success_consult_orderId_tv.setText(":   " + orderNo);

        order_pay_success_consult_name_tv.setText(":   " + name);

        order_pay_success_consult_time_tv.setText(":   " + time);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {


            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
