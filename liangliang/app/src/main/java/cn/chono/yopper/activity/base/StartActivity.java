package cn.chono.yopper.activity.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tencent.TIMCallBack;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.AppInfo;
import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.im.OfflinePushUtils;
import cn.chono.yopper.im.PushUtil;
import cn.chono.yopper.im.imEvent.MessageEvent;
import cn.chono.yopper.im.imbusiness.InitBusiness;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.im.imbusiness.TlsBusiness;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SharedprefUtil;

/**
 * @ClassName: StartActivity
 * @Description:程序的入口，启动页（闪屏页）
 * @author: xianbin.zou
 * @date: 2015年3月12日 上午9:37:21
 */
public class StartActivity extends MainFrameActivity implements TIMCallBack {


    private Context context;

    private String articleurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.start_activity);
        getTitleLayout().setVisibility(View.GONE);// 隐藏标题
        // 上下文对象
        context = this;

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("ArticleView")) {
                articleurl = bundle.getString("ArticleView");
            }
        }

        init();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("启动页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("启动页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    @Override
    public void onError(int i, String s) {

        Logger.e("login error : code " + i + " " + s);


        if(i==6023 || i==6206 || i==6207 || i==6208 || i==70013){

            if(i==6208){
                DialogUtil.showDisCoverNetToast(StartActivity.this, getString(R.string.kick_logout));
            }else{
                DialogUtil.showDisCoverNetToast(StartActivity.this, getString(R.string.login_error));
            }

            ActivityUtil.jump(StartActivity.this, SelectEntryActivity.class, null, 0, 100);

            finish();

        }else{

            if (CheckUtil.isEmpty(articleurl)) {

                ActivityUtil.jump(StartActivity.this, IndexActivity.class, null, 0, 100);

            } else {

                Bundle bundle = new Bundle();
                bundle.putString("ArticleView", articleurl);
                ActivityUtil.jump(StartActivity.this, IndexActivity.class, bundle, 0, 100);

            }

            finish();

        }

    }


    @Override
    public void onSuccess() {

        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();

        OfflinePushUtils.initPush(StartActivity.this);

        if (CheckUtil.isEmpty(articleurl)) {

            ActivityUtil.jump(StartActivity.this, IndexActivity.class, null, 0, 100);

        } else {

            Bundle bundle = new Bundle();
            bundle.putString("ArticleView", articleurl);
            ActivityUtil.jump(StartActivity.this, IndexActivity.class, bundle, 0, 100);

        }

        finish();

    }


    private void init() {

        AppInfo.getInstance().setAppMarketId(AppUtil.getChannelFromApk(this, "channelId"));

        AppInfo.getInstance().setVersionName(AppUtil.getAppVersionName(this));

        AppInfo.getInstance().setVersionCode(AppUtil.getAppVersionCode(this));

        AppInfo.getInstance().setMacAddress(AppUtil.getMacAddress(this));

        AppInfo.getInstance().setPhoneVersion(AppUtil.getPhoneVersion(this));

        // 友盟设置渠道来源
        if (CheckUtil.isEmpty(AppInfo.getInstance().getAppMarketId())) {
            //如果没有拿到渠道号则用官方
            AnalyticsConfig.setChannel("official");
            AppInfo.getInstance().setAppMarketId("official");
        } else {
            AnalyticsConfig.setChannel(AppInfo.getInstance().getAppMarketId());
        }
        // TalkingData的统计
        TalkingDataAppCpa.init(this.getApplicationContext(), "f4e091d7984441ae8a5568b5d64855f3", AppInfo.getInstance().getAppMarketId());


        /** 友盟设置是否对日志信息进行加密, 默认false(不加密). */
        AnalyticsConfig.enableEncrypt(true);
        MobclickAgent.updateOnlineConfig(this);
        PushAgent.getInstance(this).onAppStart();

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);

        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(), loglvl);
        //初始化TLS
        TlsBusiness.init(getApplicationContext());

        TIMManager.getInstance().disableStorage();

        TIMManager.getInstance().disableAutoReport();

        // 如果没有登录，则进入引导页WelcomeActivity
        String StartCode = SharedprefUtil.get(context, YpSettings.VersionName, "");

        if (TextUtils.equals(StartCode, AppInfo.getInstance().getVersionName())) {// 相同时不进引导页

            String authToken = LoginUser.getInstance().getAuthToken();

            String userSig = LoginUser.getInstance().getUserSig();

            if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(userSig)) {

                //登录之前要初始化群和好友关系链缓存
                if (YpSettings.isTest) {
                    LoginBusiness.loginIm(LoginUser.getInstance().getUserId() + "@test", userSig, this);
                } else {
                    LoginBusiness.loginIm(LoginUser.getInstance().getUserId() + "", userSig, this);
                }

            } else {

                ActivityUtil.jump(StartActivity.this, SelectEntryActivity.class, null, 0, 100);
                finish();
            }

        } else {
            // 引导页
            ActivityUtil.jump(StartActivity.this, WelcomeActivity.class, null, 0, 100);
            finish();
        }

    }

}
