package cn.chono.yopper.activity.order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.ParseException;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ActivityOrderInfo.ActivityOrderInfoReqBean;
import cn.chono.yopper.Service.Http.ActivityOrderInfo.ActivityOrderInfoRespBean;
import cn.chono.yopper.Service.Http.ActivityOrderInfo.ActivityOrderInfoService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailBean;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailDto;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailRespBean;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailService;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignBean;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignRespBean;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignResultsDto;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignService;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignBean;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignRespBean;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignRespDto;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignService;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignUnifiedDto;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.SignUpActivity.SighUpRespBean;
import cn.chono.yopper.Service.Http.SignUpActivity.SighUpService;
import cn.chono.yopper.Service.Http.SignUpActivity.SignUpReqBean;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.activity.usercenter.VipOpenedActivity;
import cn.chono.yopper.activity.usercenter.VipRenewalsActivity;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.event.OrderListEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.CommonObservable;
import cn.chono.yopper.utils.CommonObserver;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.PayResult;
import cn.chono.yopper.utils.TimeUtils;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.wxapi.WeixinUtils;

/**
 * 订单支付
 */
public class UserOrderPayActivity extends MainFrameActivity implements OnClickListener {


    private Dialog loadingDiaog;


    private TextView order_pay_timeRemaining_tv;

    private TextView order_pay_order_id_tv;

    private TextView order_pay_consultant_type_tv;

    private TextView order_pay_consultant_name_tv;


    private TextView order_pay_consultant_time_tv;

    private TextView order_pay_consultant_cost_tv;

    private LinearLayout order_pay_alipay_layout;

    private ImageView order_pay_alipay_select_iv;

    private LinearLayout order_pay_wechat_layout;

    private ImageView order_pay_wechat_select_iv;

    private ImageView order_pay_service_read_iv;

    private TextView order_pay_service_hint_tv;

    private TextView order_pay_sure_tv;

    private TextView order_pay_youhui_tv;

//-----------

    private TextView order_pay_free_tv;

    private ImageView order_pay_free_select_tv;

    private RelativeLayout freeLayout;

    private TextView order_pay_time_text_tv;

    private TextView order_pay_vip_cost_tv;

    private RelativeLayout vipcostLayout;

    private String activityId;

    private String orderId = "";

    private int payType = 0;//1是支付宝支付，2是微信支付

    private String timeStr = "";

    private String orderNo;

    private String name;

    private boolean isReadService = true;

    private static final int SDK_PAY_FLAG = 1;

    Handler mSurplusTimeHandler;

    long mTotalSecond;

    int counselType; // 咨询类型（0：塔罗 1：星盘）


    Dialog mBack_dlg;


    /**
     * 1预约时间进来的；2为我的订单
     */
    private int frompage = 0;


    Runnable mSurplusTimeRunnable = new Runnable() {
        @Override
        public void run() {

            --mTotalSecond;//每次减去一秒

            if (mTotalSecond > 0) {

                order_pay_timeRemaining_tv.setText("订单支付剩余时间" + TimeUtils.getSecondTurnHMS(mTotalSecond));

                mSurplusTimeHandler.postDelayed(this, 1000);

            } else {
                order_pay_timeRemaining_tv.setText(getResources().getString(R.string.order_pay_time_expired));
            }


        }
    };


    private void BackDialogFinish() {

        if (frompage == 1 || frompage == 2) {//预约时间过来，我的订单过来。要弹二次确认提示

            showDialog();

            return;
        }

        if (frompage == 3) {
            showDialog();
        }
        //不需要提示
        finish();

    }

    private void showDialog() {

        mBack_dlg = DialogUtil.createHintOperateDialog(UserOrderPayActivity.this, "", "确定放弃付款吗？15分钟内仍可在“我的订单”完成支付", "取消", "暂时放弃", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                mBack_dlg.dismiss();


            }

            @Override
            public void onEnsure(View view, Object... obj) {
                mBack_dlg.dismiss();

                RefreshData();
                finish();


            }
        });

        mBack_dlg.show();

    }

    private void RefreshData() {

        if (frompage == 2) {//我的订单过来。要刷新数据哪

            RxBus.get().post("OrderListEvent", new OrderListEvent());
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {


            BackDialogFinish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_order_pay);

        PushAgent.getInstance(this).onAppStart();


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(YpSettings.ORDER_ID)) {
                orderId = bundle.getString(YpSettings.ORDER_ID);
            }

            if (bundle.containsKey(YpSettings.FROM_PAGE)) {
                frompage = bundle.getInt(YpSettings.FROM_PAGE);
            }

            if (bundle.containsKey(YpSettings.ACTIVITY_ID)) {
                activityId = bundle.getString(YpSettings.ACTIVITY_ID);
            }
        }

        initView();

        if (!TextUtils.isEmpty(activityId)) {
            getActivityOrderDetail();
        } else {
            getOrderDetail();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("订单支付"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("订单支付"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    private void initView() {

        // 设置标题栏
        this.getTvTitle().setText("订单支付");

        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getBtnGoBack().setVisibility(View.VISIBLE);


        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);

                BackDialogFinish();
            }
        });

        order_pay_timeRemaining_tv = (TextView) findViewById(R.id.order_pay_timeRemaining_tv);

        order_pay_order_id_tv = (TextView) findViewById(R.id.order_pay_order_id_tv);

        order_pay_consultant_type_tv = (TextView) findViewById(R.id.order_pay_consultant_type_tv);

        order_pay_consultant_name_tv = (TextView) findViewById(R.id.order_pay_consultant_name_tv);


        order_pay_consultant_time_tv = (TextView) findViewById(R.id.order_pay_consultant_time_tv);

        order_pay_consultant_cost_tv = (TextView) findViewById(R.id.order_pay_consultant_cost_tv);

        order_pay_alipay_layout = (LinearLayout) findViewById(R.id.order_pay_alipay_layout);
        order_pay_alipay_layout.setOnClickListener(this);

        order_pay_alipay_select_iv = (ImageView) findViewById(R.id.order_pay_alipay_select_iv);

        order_pay_wechat_layout = (LinearLayout) findViewById(R.id.order_pay_wechat_layout);
        order_pay_wechat_layout.setOnClickListener(this);

        order_pay_wechat_select_iv = (ImageView) findViewById(R.id.order_pay_wechat_select_iv);

        //服务须知勾选
        order_pay_service_read_iv = (ImageView) findViewById(R.id.order_pay_service_read_iv);
        order_pay_service_read_iv.setOnClickListener(this);

        //服务须知
        order_pay_service_hint_tv = (TextView) findViewById(R.id.order_pay_service_hint_tv);
        order_pay_service_hint_tv.setOnClickListener(this);

        //确认支付
        order_pay_sure_tv = (TextView) findViewById(R.id.order_pay_sure_tv);
        order_pay_sure_tv.setOnClickListener(this);
        //-----------
        order_pay_free_tv = (TextView) findViewById(R.id.hasFee);
        order_pay_free_select_tv = (ImageView) findViewById(R.id.isFree);
        order_pay_free_select_tv.setOnClickListener(this);
        freeLayout = (RelativeLayout) findViewById(R.id.layout_hasfee);
        order_pay_time_text_tv = (TextView) findViewById(R.id.order_pay_time_text_tv);
        order_pay_vip_cost_tv = (TextView) findViewById(R.id.order_pay_vip_cost_tv);
        vipcostLayout = (RelativeLayout) findViewById(R.id.vip_cost_layout);
    }


    boolean isFree;
    Dialog mSignUpDialog;
    Dialog mSignOkDialog;

    @Override
    public void onClick(View v) {


        int id = v.getId();
        switch (id) {
            case R.id.youhui:
                //根据登陆用户的VIP状态跳转页面
                //若没有开通过VIP，点击进入VIP介绍页
                //若VIP已过期，点击进入续费页面

                Bundle bundle = new Bundle();

                int userPosition = DbHelperUtils.getOldVipPosition(LoginUser.getInstance().getUserId());

                bundle.putInt("userPosition", userPosition);

                if (0 == userPosition) {
                    ActivityUtil.jump(UserOrderPayActivity.this, VipOpenedActivity.class, bundle, 0, 100);
                } else {
                    ActivityUtil.jump(UserOrderPayActivity.this, VipRenewalsActivity.class, bundle, 0, 100);
                }
                break;
            case R.id.isFree:
                isFree = !isFree;
                order_pay_free_select_tv.setBackgroundResource(isFree ? R.drawable.ic_pay_type_selected : R.drawable.ic_pay_type_default);

                break;

            case R.id.order_pay_alipay_layout://支付宝支付

                payType = 1;
                updatePayTypeView(payType);

                break;

            case R.id.order_pay_wechat_layout://微信支付

                payType = 2;
                updatePayTypeView(payType);

                break;

            case R.id.order_pay_service_read_iv://是否阅读服务

                if (isReadService) {

                    order_pay_service_read_iv.setImageResource(R.drawable.evaluation_default_icon);

                    isReadService = false;

                } else {

                    order_pay_service_read_iv.setImageResource(R.drawable.evaluation_selected_icon);

                    isReadService = true;

                }


                break;

            case R.id.order_pay_service_hint_tv://支付服务须知

                Bundle bdS = new Bundle();
                if (!TextUtils.isEmpty(activityId)) {
                    bdS.putString(YpSettings.BUNDLE_KEY_WEB_URL, "help/service");
                } else {
                    bdS.putString(YpSettings.BUNDLE_KEY_WEB_URL, "help/tarot");
                }

                bdS.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);
                bdS.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "服务须知");
                ActivityUtil.jump(UserOrderPayActivity.this, SimpleWebViewActivity.class, bdS, 0, 100);

                break;


            case R.id.order_pay_sure_tv://确认支付


                if (counselType == 0) {
                    MobclickAgent.onEvent(UserOrderPayActivity.this, "btn_payment_event_Pay_Tarot");
                } else if (counselType == 1) {
                    MobclickAgent.onEvent(UserOrderPayActivity.this, "btn_payment_event_Pay_Astrolabe");

                }

                if (isFree) {
                    mSignUpDialog = DialogUtil.createHintOperateDialog(this, "", "您确定使用免费机会参加活动吗?", "取消", "确定", new BackCallListener() {
                        @Override
                        public void onCancel(View view, Object... obj) {
                            mSignUpDialog.dismiss();
                        }

                        @Override
                        public void onEnsure(View view, Object... obj) {
                            signUp();
                        }
                    });
                    mSignUpDialog.show();
                    return;
                }

                if (payType == 0) {
                    DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, "请选择支付方式");
                    return;
                }


                if (isReadService) {

                    if (payType == 1) {

                        getSign();
//                        doAlipay("");

                    } else if (payType == 2) {

                        //判断是否支付微信支付
                        if (WeixinUtils.isWeixinPay()) {

                            getWXSignInfo();

                        } else {

                            DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, "微信版本不支持支付");
                        }
                    }


                } else {
                    DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, "请确认是否阅读服务须知");
                }

                break;

        }
    }

    Dialog okDialog;

    void signUp() {
        final SignUpReqBean reqBean = new SignUpReqBean();
        reqBean.setId(activityId);
        reqBean.setUseFee(true);
        SighUpService service = new SighUpService(UserOrderPayActivity.this);
        service.parameter(reqBean);
        service.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                SighUpRespBean bean = (SighUpRespBean) respBean;
                // 报名结果（0：成功 1：手机号未认证 2：资料不完善 3：重复报名 4：报名人数已满 5：超过报名截止时间）
                switch (bean.getResp().getResult()) {
                    case 0:
//                        Toast.makeText(UserOrderPayActivity.this,bean.getResp().getMsg(),Toast.LENGTH_SHORT).show();
//                        Bundle bundle = new Bundle();
//
//                        bundle.putInt(YpSettings.ORDER_TYPE, 3);
//                        bundle.putString(YpSettings.ORDER_NO, orderNo);
//                        bundle.putString(YpSettings.CONSULT_NAME, name);
//                        bundle.putString(YpSettings.CONSULT_TIME, timeStr);
//                        bundle.putInt(YpSettings.COUNSEL_TYPE, ImConstant.CounselorType_Activity);
//
//
//                        LogUtils.e("APPLE_NUM=" + orderNo);
//                        LogUtils.e("ORDER_NO=" + name);
//                        LogUtils.e("time=" + timeStr);
//                        LogUtils.e("counselType=" + ImConstant.CounselorType_Activity);
//
//                        ActivityUtil.jump(UserOrderPayActivity.this, UserOrderPaySuccessActivity.class, bundle, 0, 100);
//
//                        finish();
                        mSignUpDialog.dismiss();
                        okDialog = DialogUtil.createHintOperateDialog(UserOrderPayActivity.this, "", "报名成功", "", "确定", new BackCallListener() {
                            @Override
                            public void onCancel(View view, Object... obj) {

                            }

                            @Override
                            public void onEnsure(View view, Object... obj) {
                                finish();
                            }
                        });
                        okDialog.show();
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        Toast.makeText(UserOrderPayActivity.this, bean.getResp().getMsg(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
            }
        });
        service.enqueue();
    }

    private void updatePayTypeView(int type) {

        if (type == 1) {
            order_pay_alipay_select_iv.setBackgroundResource(R.drawable.ic_pay_type_selected);
            order_pay_wechat_select_iv.setBackgroundResource(R.drawable.ic_pay_type_default);
        } else if (type == 2) {
            order_pay_alipay_select_iv.setBackgroundResource(R.drawable.ic_pay_type_default);
            order_pay_wechat_select_iv.setBackgroundResource(R.drawable.ic_pay_type_selected);
        }

    }

    private void setViewData(OrderDetailDto dto) {

        if (!CheckUtil.isEmpty(dto.getOrderNo())) {

            orderNo = CheckUtil.splitStringWithNum(dto.getOrderNo(), 4);

            order_pay_order_id_tv.setText(orderNo);
        }

        counselType = dto.getCounselType();

        if (counselType == 0) {
            order_pay_consultant_type_tv.setText("塔罗师");
        } else {
            order_pay_consultant_type_tv.setText("占星师");
        }

        if (dto.getUser() != null && !CheckUtil.isEmpty(dto.getUser().getName())) {
            name = dto.getUser().getName();
            order_pay_consultant_name_tv.setText(name);
        }

//        long bookTime = ISO8601.getTime(dto.getBookingTime());

        long bookTime = TimeUtils.getFormat(dto.getBookingTime());


        timeStr = TimeUtils.longToString(bookTime, "yyyy-MM-dd HH:mm:ss");

        order_pay_consultant_time_tv.setText(timeStr);

        StringBuilder sb = new StringBuilder("￥");

        sb.append((dto.getTotalFee() / 100));

        order_pay_consultant_cost_tv.setText(sb.toString());


        mTotalSecond = dto.getRemainPaymentSeconds();


        //无剩余时间
        if (mTotalSecond == 0) {
            order_pay_timeRemaining_tv.setText(getResources().getString(R.string.order_pay_time_expired));
            return;
        }
        //如果没有过期
        //开启倒计时
        mSurplusTimeHandler = new Handler();
        mSurplusTimeHandler.postDelayed(mSurplusTimeRunnable, 1000);

    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        if (!TextUtils.isEmpty(activityId)) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(YpSettings.ORDER_TYPE, 3);
                            bundle.putString(YpSettings.ORDER_NO, orderNo);
                            bundle.putString(YpSettings.CONSULT_NAME, name);
                            bundle.putString(YpSettings.CONSULT_TIME, timeStr);
                            bundle.putInt(YpSettings.COUNSEL_TYPE, Constant.CounselorType_Activity);


                            LogUtils.e("APPLE_NUM=" + orderNo);
                            LogUtils.e("ORDER_NO=" + name);
                            LogUtils.e("time=" + timeStr);
                            LogUtils.e("counselType=" + Constant.CounselorType_Activity);

                            ActivityUtil.jump(UserOrderPayActivity.this, UserOrderPaySuccessActivity.class, bundle, 0, 100);

                            finish();
                        } else {
                            Bundle bundle = new Bundle();

                            bundle.putInt(YpSettings.ORDER_TYPE, 0);

                            bundle.putString(YpSettings.ORDER_NO, orderNo);
                            bundle.putString(YpSettings.CONSULT_NAME, name);
                            bundle.putString(YpSettings.CONSULT_TIME, timeStr);
                            bundle.putInt(YpSettings.COUNSEL_TYPE, counselType);


                            LogUtils.e("APPLE_NUM=" + orderNo);
                            LogUtils.e("ORDER_NO=" + name);
                            LogUtils.e("time=" + timeStr);
                            LogUtils.e("counselType=" + counselType);


                            ActivityUtil.jump(UserOrderPayActivity.this, UserOrderPaySuccessActivity.class, bundle, 0, 100);

                            finish();
                        }

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                            DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, "支付失败");

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };


    private Runnable weiXinPayRunnable = new Runnable() {
        @Override
        public void run() {

            Bundle bundle = new Bundle();
            bundle.putInt(YpSettings.ORDER_TYPE, 0);

            bundle.putString(YpSettings.ORDER_NO, orderNo);
            bundle.putString(YpSettings.CONSULT_NAME, name);
            bundle.putString(YpSettings.CONSULT_TIME, timeStr);

            ActivityUtil.jump(UserOrderPayActivity.this, UserOrderPaySuccessActivity.class, bundle, 0, 100);

            finish();
        }
    };

    private CommonObserver.WeiXinPayObserver weiXinPayObserver = new CommonObserver.WeiXinPayObserver(weiXinPayRunnable);


    /**
     * 支付宝支付前置条件判断以及获取签名
     */
    private void getSign() {

        loadingDiaog = DialogUtil.LoadingDialog(UserOrderPayActivity.this, null);

        if (!isFinishing()) {
            loadingDiaog.show();
        }

        AlipayGetSignService dataService = new AlipayGetSignService(this);

        AlipayGetSignBean alipayGetSignBean = new AlipayGetSignBean();

        alipayGetSignBean.setOut_trade_no(orderId);

        dataService.parameter(alipayGetSignBean);

        dataService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                AlipayGetSignRespBean alipayGetSignRespBean = (AlipayGetSignRespBean) respBean;

                AlipayGetSignResultsDto dto = alipayGetSignRespBean.getResp();

                if (dto.getResult() == 0) {
                    doAlipay(dto.getSign());
                    return;
                }

                DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, dto.getMsg());


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();


                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, msg);

            }
        });
        dataService.enqueue();

    }


    /**
     * 微信支付前置条件判断以及获取签名等订单信息
     */
    private void getWXSignInfo() {

        loadingDiaog = DialogUtil.LoadingDialog(UserOrderPayActivity.this, null);

        if (!isFinishing()) {
            loadingDiaog.show();
        }

        WXGetSignService wXGetSignService = new WXGetSignService(this);

        WXGetSignBean wXGetSignBean = new WXGetSignBean();

        wXGetSignBean.setOrderid(orderId);

        wXGetSignService.parameter(wXGetSignBean);

        wXGetSignService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                WXGetSignRespBean wXGetSignRespBean = (WXGetSignRespBean) respBean;

                WXGetSignRespDto dto = wXGetSignRespBean.getResp();

                if (dto.getCheckResult() == 0) {

                    WXGetSignUnifiedDto wxGetSignUnifiedDto = dto.getWxUnifiedResp();

                    if (wxGetSignUnifiedDto != null) {

                        if (wxGetSignUnifiedDto.isSuccess()) {

                            CommonObservable.getInstance().addObserver(weiXinPayObserver);
                            WeixinUtils.WeixinPay(wxGetSignUnifiedDto.getPrePayId(), wxGetSignUnifiedDto.getNonce_str(), wxGetSignUnifiedDto.getTimeStamp(), wxGetSignUnifiedDto.getSign());

                        } else {
                            DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, wxGetSignUnifiedDto.getErrorMsg());
                            return;
                        }
                    } else {
                        DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this);
                        return;
                    }

                    return;
                }

                DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, dto.getMsg());


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, msg);

            }
        });
        wXGetSignService.enqueue();

    }


    /**
     * 调用支付宝发送支付请求
     *
     * @param
     */
    private void doAlipay(final String payInfo) {


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(UserOrderPayActivity.this);
                // 调用支付接口，获取支付结果

                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    void getActivityOrderDetail() {
        loadingDiaog = DialogUtil.LoadingDialog(UserOrderPayActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }
        final ActivityOrderInfoReqBean reqBean = new ActivityOrderInfoReqBean();
        reqBean.setId(orderId);
        ActivityOrderInfoService service = new ActivityOrderInfoService(this);
        service.parameter(reqBean);
        service.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();
                ActivityOrderInfoRespBean bean = (ActivityOrderInfoRespBean) respBean;
                setActivityInfo(bean);
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, msg);
            }
        });
        service.enqueue();
    }

    void setActivityInfo(ActivityOrderInfoRespBean respBean) {
        ActivityOrderInfoRespBean.ActivityOrderInfo info = respBean.getResp();


        if (!CheckUtil.isEmpty(info.getOrderId())) {

            orderNo = CheckUtil.splitStringWithNum(info.getOrderNo(), 4);

            order_pay_order_id_tv.setText(orderNo);
        }
        order_pay_timeRemaining_tv.setVisibility(View.GONE);
        order_pay_youhui_tv = (TextView) findViewById(R.id.youhui);
        order_pay_youhui_tv.setVisibility(View.VISIBLE);
        order_pay_youhui_tv.setOnClickListener(this);
        order_pay_consultant_type_tv.setText("活动名称");
        order_pay_consultant_name_tv.setText(respBean.getResp().getActivity().getTitle());
        name = respBean.getResp().getActivity().getTitle();
        order_pay_time_text_tv.setText("活动时间");

        long bookTime = TimeUtils.getFormat(respBean.getResp().getActivity().getActivityStartTime());

        timeStr = TimeUtils.longToString(bookTime, "yyyy-MM-dd HH:mm:ss");

        order_pay_consultant_time_tv.setText(timeStr);
        order_pay_consultant_time_tv.setTextColor(getResources().getColor(R.color.color_666666));
        order_pay_consultant_cost_tv.setText("¥" + respBean.getResp().getActivity().getFee() / 100 + "");
        order_pay_consultant_cost_tv.setTextColor(getResources().getColor(R.color.color_666666));


        if (respBean.getResp().getActivity().isAllowFreeByMember()) {
            freeLayout.setVisibility(View.VISIBLE);
            order_pay_free_tv.setText("还有" + respBean.getResp().getFreeOfflineActivityCount() + "次免费参与次数");
            if (respBean.getResp().getFreeOfflineActivityCount() == 0) {
                order_pay_free_select_tv.setClickable(false);
            } else {
                order_pay_free_select_tv.setClickable(true);
            }
        } else {
            freeLayout.setVisibility(View.GONE);
        }

        if (respBean.getResp().getVipFee() != 0) {
            vipcostLayout.setVisibility(View.VISIBLE);
            order_pay_vip_cost_tv.setText("¥" + respBean.getResp().getVipFee() / 100 + "  (" + respBean.getResp().getDiscount() + "折)");
        } else {
            vipcostLayout.setVisibility(View.GONE);
        }

    }

    /**
     * 获取订单详情
     */
    private void getOrderDetail() {

        loadingDiaog = DialogUtil.LoadingDialog(UserOrderPayActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        OrderDetailService orderDetailService = new OrderDetailService(this);

        OrderDetailBean orderDetailBean = new OrderDetailBean();

        orderDetailBean.setId(orderId);

        orderDetailService.parameter(orderDetailBean);

        orderDetailService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                OrderDetailRespBean orderDetailRespBean = (OrderDetailRespBean) respBean;

                OrderDetailDto dto = orderDetailRespBean.getResp();

                setViewData(dto);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();


                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(UserOrderPayActivity.this, msg);

            }
        });
        orderDetailService.enqueue();

    }


    @Override
    protected void onDestroy() {
        CommonObservable.getInstance().deleteObserver(weiXinPayObserver);
        super.onDestroy();
    }

}
