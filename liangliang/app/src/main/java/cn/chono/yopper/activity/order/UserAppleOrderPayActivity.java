package cn.chono.yopper.activity.order;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignBean;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignRespBean;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignResultsDto;
import cn.chono.yopper.Service.Http.OrderPayWithAlipayGetSign.AlipayGetSignService;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignBean;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignRespBean;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignRespDto;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignUnifiedDto;
import cn.chono.yopper.Service.Http.OrderPayWithWXGetInfo.WXGetSignService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.GameOrderEvent;
import cn.chono.yopper.event.GiftEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CommonObservable;
import cn.chono.yopper.utils.CommonObserver;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.PayResult;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.wxapi.WeixinUtils;

/**
 * 购买苹果订单支付
 */
public class UserAppleOrderPayActivity extends MainFrameActivity implements OnClickListener {


    private Dialog loadingDiaog;

    private ImageView apple_game_pay_iv;

    private TextView apple_pay_num_tv;

    private TextView apple_pay_cost_tv;

    private LinearLayout apple_pay_alipay_layout;

    private LinearLayout apple_pay_wechat_layout;

    private String orderId = "";

    private long cost;

    private String productName;


    private static final int SDK_PAY_FLAG = 1;

    //1表示苹果购买 2表示星钻购买；3表示礼物页面苹果快速购买，4表示聊天页面快速购买
    private int payType = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_apple_order_pay);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(YpSettings.ORDER_ID)) {
                orderId = bundle.getString(YpSettings.ORDER_ID);
            }

            if (bundle.containsKey(YpSettings.ProductName)) {
                productName = bundle.getString(YpSettings.ProductName);
            }

            if (bundle.containsKey(YpSettings.PAY_COST)) {
                cost = bundle.getLong(YpSettings.PAY_COST);
            }

            if (bundle.containsKey(YpSettings.PAY_TYPE)) {
                payType = bundle.getInt(YpSettings.PAY_TYPE);
            }

        }
        initView();
        setViewData();

    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("支付方式"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("支付方式"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    private void initView() {

        // 设置标题栏
        this.getTvTitle().setText("支付方式");

        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getBtnGoBack().setVisibility(View.VISIBLE);


        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });


        apple_game_pay_iv = (ImageView) findViewById(R.id.apple_game_pay_iv);

        apple_pay_num_tv = (TextView) findViewById(R.id.apple_pay_num_tv);

        apple_pay_cost_tv = (TextView) findViewById(R.id.apple_pay_cost_tv);

        //服务须知
        apple_pay_alipay_layout = (LinearLayout) findViewById(R.id.apple_pay_alipay_layout);
        apple_pay_alipay_layout.setOnClickListener(this);

        //确认支付
        apple_pay_wechat_layout = (LinearLayout) findViewById(R.id.apple_pay_wechat_layout);
        apple_pay_wechat_layout.setOnClickListener(this);

        if (payType == 1 || payType == 3 || payType == 4) {
            apple_game_pay_iv.setBackgroundResource(R.drawable.ic_apple);
        } else {
            apple_game_pay_iv.setBackgroundResource(R.drawable.ic_starjewel);
        }
    }

    @Override
    public void onClick(View v) {


        int id = v.getId();
        switch (id) {

            case R.id.apple_pay_alipay_layout://支付宝支付

                getSign();
                break;

            case R.id.apple_pay_wechat_layout://微信支付

                //判断是否支付微信支付
                if (WeixinUtils.isWeixinPay()) {

                    getWXSignInfo();

                } else {

                    DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, "微信版本不支持支付");
                }
                break;

        }
    }


    private void setViewData() {


        apple_pay_num_tv.setText(productName);

        apple_pay_cost_tv.setText(cost + "元");


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


                        if (payType == 3) {
                            //礼物页面快速购买苹果成功后，返回
                            RxBus.get().post("quicklyPaySuccessForGift", new CommonEvent<>());
                            finish();
                            return;
                        }

                        if (payType == 4) {
                            //礼物页面快速购买苹果成功后，返回
                            RxBus.get().post("quicklyPaySuccessForChat", new CommonEvent<>());
                            finish();
                            return;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putInt(YpSettings.ORDER_TYPE, payType);
                        bundle.putString(YpSettings.ProductName, productName);

                        ActivityUtil.jump(UserAppleOrderPayActivity.this, UserOrderPaySuccessActivity.class, bundle, 0, 100);

                        finish();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                            DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, "支付失败");
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

            if (payType == 3) {
                //礼物页面快速购买苹果成功后，返回
                RxBus.get().post("quicklyPaySuccessForGift", new CommonEvent<>());
                finish();
                return;
            }

            if (payType == 4) {
                //礼物页面快速购买苹果成功后，返回
                RxBus.get().post("quicklyPaySuccessForChat", new CommonEvent<>());
                finish();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putInt(YpSettings.ORDER_TYPE, payType);
            bundle.putString(YpSettings.ProductName, productName);
            ActivityUtil.jump(UserAppleOrderPayActivity.this, UserOrderPaySuccessActivity.class, bundle, 0, 100);

            finish();
        }
    };

    private CommonObserver.WeiXinPayObserver weiXinPayObserver = new CommonObserver.WeiXinPayObserver(weiXinPayRunnable);


    /**
     * 支付宝支付前置条件判断以及获取签名
     */
    private void getSign() {

        loadingDiaog = DialogUtil.LoadingDialog(UserAppleOrderPayActivity.this, null);

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

                DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, dto.getMsg());

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();


                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, msg);

            }
        });
        dataService.enqueue();

    }


    /**
     * 微信支付前置条件判断以及获取签名等订单信息
     */
    private void getWXSignInfo() {

        loadingDiaog = DialogUtil.LoadingDialog(UserAppleOrderPayActivity.this, null);

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
                            DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, wxGetSignUnifiedDto.getErrorMsg());
                            return;
                        }
                    } else {
                        DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this);
                        return;
                    }

                    return;
                }

                DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, dto.getMsg());

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(UserAppleOrderPayActivity.this, msg);

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
                PayTask alipay = new PayTask(UserAppleOrderPayActivity.this);
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

    @Override
    protected void onDestroy() {
        CommonObservable.getInstance().deleteObserver(weiXinPayObserver);
        super.onDestroy();
    }


}
