package cn.chono.yopper.activity.base;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMManager;
import com.tencent.TIMUserStatusListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.DaillyTask.DaillyTask;
import cn.chono.yopper.Service.DaillyTask.OnDaillTaskListener;
import cn.chono.yopper.Service.DaillyTaskService.DaillyTaskService;
import cn.chono.yopper.Service.DaillyTaskService.OnDaillyTaskServiceListener;
import cn.chono.yopper.Service.Http.GainVersionInfo.GainVersionInfoRespBean;
import cn.chono.yopper.Service.Http.GainVersionInfo.GainVersionInfoService;
import cn.chono.yopper.Service.Http.OAuthToken.OAuthTokenRespBean;
import cn.chono.yopper.Service.Http.OAuthToken.OAuthTokenService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoRespBean;
import cn.chono.yopper.Service.Http.UserInfo.UserInfoService;
import cn.chono.yopper.Service.Http.UserSync.UserSyncBean;
import cn.chono.yopper.Service.Http.UserSync.UserSyncRespBean;
import cn.chono.yopper.Service.Http.UserSync.UserSyncService;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.find.ArticleContentDetailActivity;
import cn.chono.yopper.data.BaseUser;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.LoginVideoStatusDto;
import cn.chono.yopper.data.SyncDto;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.VersionChkDTO;
import cn.chono.yopper.data.Visits;
import cn.chono.yopper.entity.chatgift.GiftUtil;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.GameTokenEvent;
import cn.chono.yopper.event.OnTopEvent;
import cn.chono.yopper.event.SyncVideoStateEvent;
import cn.chono.yopper.im.imEvent.MessageEvent;
import cn.chono.yopper.im.imObserver.ConversationObserver;
import cn.chono.yopper.im.imbusiness.LoginBusiness;
import cn.chono.yopper.im.imbusiness.TlsBusiness;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.task.RemoveAliasAsyncTask;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.CommonObservable;
import cn.chono.yopper.utils.CommonObserver;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SharedprefUtil;

public class IndexActivity extends MainFrameActivity implements OnClickListener, TIMCallBack {

    private View contentView;

    // 定义Fragment页面
    private DiscoverListFragment fragment_nearby;
    private KingdomFragment fragment_kingdom;
    private UserCenterFragment fragment_bump;
    private FindFragment fragment_find;
    private ActivitiesFragment fragment_activity;

    // 首页底部约会按钮
    private LinearLayout activity_layout_indexActivity;


    private LinearLayout find_layout_indexActivity;

    private LinearLayout usercenter_layout_indexActivity;

    private ImageView icon_usercenter_narmal_icon;

    private ImageView icon_activity_icon;

    private ImageView icon_find_narmal_icon;

    private ImageView icon_nearby_narmal_icon;

    private ImageView icon_look_me_iv;

    private TextView icon_nearby_narmal_tv;
    private TextView icon_activity_narmal_tv;
    private TextView icon_find_narmal_tv;
    private TextView icon_kingdom_narmal_tv;
    private TextView icon_usercenter_narmal_tv;

    // 首页消息按钮
    private LinearLayout kingdom_layout_indexActivity;


    // 附近按钮
    private LinearLayout nearby_layout_indexActivity;

    // 升级
    private Dialog versiondialog;

    private String DownloadLink;

    // 版本升级监听
    private VersionChkDTO checkVerDTO;

    // 版本升级监听
    private Runnable newVersionRun = new Runnable() {

        @Override
        public void run() {

            dealWithVersionDto();
        }
    };

    private CommonObserver.NewVersionObserver newVersionObserver = new CommonObserver.NewVersionObserver(newVersionRun);

    public static int selected_menu_type = 1;

    private PackageManager packageManager;
    private PackageInfo packInfo;

    private FragmentTransaction fragmentTransaction;

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    private int userid;

    private long mExitTime;

    private Handler synchandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                getSync();
            }
        }
    };

    private Dialog loadingDiaog;

    private ConversationObserver mConversationObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
        contentView = View.inflate(this, R.layout.index, null);

        setContentView(contentView);

        Loc.sendLocControlMessage(true);

        selected_menu_type = 0;

        userid = LoginUser.getInstance().getUserId();


        // 初始化界面
        initComponent();


        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {

                if (userid != 0) {
                    RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();
                    loginAsyncTask.execute("账号在另外一台设备登录了");
                }

            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                if (userid != 0) {
                    RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();
                    loginAsyncTask.execute("登录过期，请重新登录");
                }

            }
        });

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();


        BaseUser baseUser = DbHelperUtils.getBaseUser(userid);
        if (baseUser != null) {
            find_layout_indexActivity.performClick();
        } else {
            loadingDiaog = DialogUtil.LoadingDialog(IndexActivity.this, null);
            if (!isFinishing()) {
                loadingDiaog.show();
            }
        }
        getUserInfo();

        // 注册监听
        CommonObservable.getInstance().addObserver(newVersionObserver);

        getVersionInfo();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("ArticleView")) {
                String articleurl = bundle.getString("ArticleView");
                find_layout_indexActivity.performClick();
                Bundle bd = new Bundle();
                bd.putString(YpSettings.BUNDLE_KEY_WEB_URL, articleurl);
                bd.putString(YpSettings.BUNDLE_KEY_WEB_URL_TYPE, "Article");
                bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

                ActivityUtil.jump(IndexActivity.this, ArticleContentDetailActivity.class, bd, 0, 100);
            }
        }

        mConversationObserver = new ConversationObserver();

        setConnectionListener();

        //获取奖品
        GiftUtil giftUtil = new GiftUtil();
        giftUtil.getNetGiftData(LoginUser.getInstance().getUserId());

    }

    @Override
    public void onResume() {
        super.onResume();


        startTimer();


        if (selected_menu_type != 1 && selected_menu_type != 2) {
            Loc.sendLocControlMessage(false);
        } else {
            Loc.sendLocControlMessage(true);
        }
//


        /**
         * 每日钥匙
         */
        int selected_tab_Id = SharedprefUtil.getInt(IndexActivity.this, YpSettings.DISCOVER_TAB_ID, 0);
        getDaillyTaskKeys(selected_menu_type, selected_tab_Id);

    }


    @Override
    protected void onPause() {
        super.onPause();
        Loc.sendLocControlMessage(false);
        stopTimer();
    }

    @Override
    protected void onDestroy() {
        // // 注销
        RxBus.get().unregister(this);

        mConversationObserver.stop();

//        if(YpSettings.isTest){
//            TlsBusiness.logout(userid+"@test");
//        }else{
//            TlsBusiness.logout(userid+"");
//        }


        MessageEvent.getInstance().clear();

        super.onDestroy();


    }

    private void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }


    private void startTimer() {

        stopTimer();

        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    synchandler.sendMessage(msg);
                }
            };
        }

        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, 0, 2 * 60 * 1000);

    }


    /**
     * 初始化界面
     */
    FragmentManager fragmentMag;

    private void initComponent() {


        nearby_layout_indexActivity = (LinearLayout) this.findViewById(R.id.nearby_layout_indexActivity);

        activity_layout_indexActivity = (LinearLayout) this.findViewById(R.id.activity_layout_indexActivity);

        find_layout_indexActivity = (LinearLayout) this.findViewById(R.id.find_layout_indexActivity);

        kingdom_layout_indexActivity = (LinearLayout) this.findViewById(R.id.kingdom_layout_indexActivity);

        usercenter_layout_indexActivity = (LinearLayout) this.findViewById(R.id.usercenter_layout_indexActivity);


        icon_nearby_narmal_icon = (ImageView) this.findViewById(R.id.icon_nearby_narmal_icon);

        icon_activity_icon = (ImageView) this.findViewById(R.id.icon_activity_icon);

        icon_find_narmal_icon = (ImageView) this.findViewById(R.id.icon_find_narmal_icon);


        icon_usercenter_narmal_icon = (ImageView) this.findViewById(R.id.icon_usercenter_narmal_icon);


        icon_nearby_narmal_tv = (TextView) findViewById(R.id.icon_nearby_narmal_tv);

        icon_activity_narmal_tv = (TextView) findViewById(R.id.icon_activity_narmal_tv);

        icon_find_narmal_tv = (TextView) findViewById(R.id.icon_find_narmal_tv);

        icon_kingdom_narmal_tv = (TextView) findViewById(R.id.icon_kingdom_narmal_tv);

        icon_usercenter_narmal_tv = (TextView) findViewById(R.id.icon_usercenter_narmal_tv);


        icon_look_me_iv = (ImageView) this.findViewById(R.id.icon_look_me_iv);


        fragmentMag = getSupportFragmentManager();

        nearby_layout_indexActivity.setOnClickListener(this);


        activity_layout_indexActivity.setOnClickListener(this);

        find_layout_indexActivity.setOnClickListener(this);

        kingdom_layout_indexActivity.setOnClickListener(this);

        usercenter_layout_indexActivity.setOnClickListener(this);


//        contentView.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                appointment_layout_indexActivity.performClick();
//            }
//        }, 10);

    }

    @Override
    public void onClick(View v) {

//        setFullScreen(false);
        int id = v.getId();
        switch (id) {

            case R.id.nearby_layout_indexActivity://附近
                Loc.sendLocControlMessage(true);
                int selected_tab_Id = SharedprefUtil.getInt(IndexActivity.this, YpSettings.DISCOVER_TAB_ID, 0);
                if (selected_menu_type == 2) {

                    if (selected_tab_Id == 0) {

                        RxBus.get().post("OnTopEvent", new OnTopEvent(1));

                    } else if (selected_tab_Id == 2) {
                        RxBus.get().post("OnTopEvent", new OnTopEvent(2));
                    }
                    return;
                }

                MobclickAgent.onEvent(IndexActivity.this, "btn_lialia");

                selected_menu_type = 2;

                setChioceItem(2);
                // 改变选中状态


                nearby_layout_indexActivity.setSelected(true);
                icon_nearby_narmal_icon.setSelected(true);
                icon_nearby_narmal_tv.setSelected(true);

                activity_layout_indexActivity.setSelected(false);
                icon_activity_icon.setSelected(false);
                icon_activity_narmal_tv.setSelected(false);

                find_layout_indexActivity.setSelected(false);
                icon_find_narmal_icon.setSelected(false);
                icon_find_narmal_tv.setSelected(false);

                kingdom_layout_indexActivity.setSelected(false);

                icon_kingdom_narmal_tv.setSelected(false);

                usercenter_layout_indexActivity.setSelected(false);
                icon_usercenter_narmal_icon.setSelected(false);
                icon_usercenter_narmal_tv.setSelected(false);
                /**
                 * 附近人打分任务
                 */
                getDaillyTaskKeys(selected_menu_type, selected_tab_Id);

                break;


            case R.id.activity_layout_indexActivity:// 活动

                Loc.sendLocControlMessage(true);

                if (selected_menu_type == 4) {
                    return;
                }

                MobclickAgent.onEvent(IndexActivity.this, "btn_activity");

                selected_menu_type = 4;

                setChioceItem(4);
                // 改变选中状态

                nearby_layout_indexActivity.setSelected(false);
                icon_nearby_narmal_icon.setSelected(false);
                icon_nearby_narmal_tv.setSelected(false);

                activity_layout_indexActivity.setSelected(true);
                icon_activity_icon.setSelected(true);
                icon_activity_narmal_tv.setSelected(true);

                find_layout_indexActivity.setSelected(false);
                icon_find_narmal_icon.setSelected(false);
                icon_find_narmal_tv.setSelected(false);

                kingdom_layout_indexActivity.setSelected(false);

                icon_kingdom_narmal_tv.setSelected(false);

                usercenter_layout_indexActivity.setSelected(false);
                icon_usercenter_narmal_icon.setSelected(false);
                icon_usercenter_narmal_tv.setSelected(false);


                break;

            case R.id.find_layout_indexActivity:// 发现


                Loc.sendLocControlMessage(true);

                if (selected_menu_type == 1) {
                    return;
                }

                MobclickAgent.onEvent(IndexActivity.this, "btn_find");

                selected_menu_type = 1;

                setChioceItem(1);


                nearby_layout_indexActivity.setSelected(false);
                icon_nearby_narmal_icon.setSelected(false);
                icon_nearby_narmal_tv.setSelected(false);

                activity_layout_indexActivity.setSelected(false);
                icon_activity_icon.setSelected(false);
                icon_activity_narmal_tv.setSelected(false);

                find_layout_indexActivity.setSelected(true);
                icon_find_narmal_icon.setSelected(true);
                icon_find_narmal_tv.setSelected(true);

                kingdom_layout_indexActivity.setSelected(false);

                icon_kingdom_narmal_tv.setSelected(false);

                usercenter_layout_indexActivity.setSelected(false);
                icon_usercenter_narmal_icon.setSelected(false);
                icon_usercenter_narmal_tv.setSelected(false);


                break;


            case R.id.kingdom_layout_indexActivity:// 游戏王国

                MobclickAgent.onEvent(IndexActivity.this, "btn_kingdom");

//                setFullScreen(true);

                Loc.sendLocControlMessage(false);

                if (selected_menu_type == 3) {
                    return;
                }
                selected_menu_type = 3;

                setChioceItem(3);
                // 改变选中状态
                getOAuthToken();

                nearby_layout_indexActivity.setSelected(false);
                icon_nearby_narmal_icon.setSelected(false);
                icon_nearby_narmal_tv.setSelected(false);

                activity_layout_indexActivity.setSelected(false);
                icon_activity_icon.setSelected(false);
                icon_activity_narmal_tv.setSelected(false);


                find_layout_indexActivity.setSelected(false);
                icon_find_narmal_icon.setSelected(false);
                icon_find_narmal_tv.setSelected(false);

                kingdom_layout_indexActivity.setSelected(true);

                icon_kingdom_narmal_tv.setSelected(true);

                usercenter_layout_indexActivity.setSelected(false);
                icon_usercenter_narmal_icon.setSelected(false);
                icon_usercenter_narmal_tv.setSelected(false);
                break;

            case R.id.usercenter_layout_indexActivity:// 个人中心

                MobclickAgent.onEvent(IndexActivity.this, "btn_usercenter");


                Loc.sendLocControlMessage(false);

                if (selected_menu_type == 5) {
                    return;
                }
                selected_menu_type = 5;

                setChioceItem(5);
                // 改变选中状态
                nearby_layout_indexActivity.setSelected(false);
                icon_nearby_narmal_icon.setSelected(false);
                icon_nearby_narmal_tv.setSelected(false);

                activity_layout_indexActivity.setSelected(false);
                icon_activity_icon.setSelected(false);
                icon_activity_narmal_tv.setSelected(false);


                find_layout_indexActivity.setSelected(false);
                icon_find_narmal_icon.setSelected(false);
                icon_find_narmal_tv.setSelected(false);

                kingdom_layout_indexActivity.setSelected(false);

                icon_kingdom_narmal_tv.setSelected(false);

                usercenter_layout_indexActivity.setSelected(true);
                icon_usercenter_narmal_icon.setSelected(true);
                icon_usercenter_narmal_tv.setSelected(true);

                break;

            default:
                break;
        }

    }

    /**
     * 选项卡索引
     */
    public void setChioceItem(int index) {
        // 重置选项+隐藏所有Fragment
        fragmentTransaction = fragmentMag.beginTransaction();

        hideFragments(fragmentTransaction);
        switch (index) {
            case 2:// 附近

                if (fragment_nearby != null) {
                    fragmentTransaction.show(fragment_nearby);
                } else {
                    fragment_nearby = new DiscoverListFragment();
                    fragmentTransaction.add(R.id.frame_content, fragment_nearby);
                }
                break;

            case 4:// 活动

                if (fragment_activity != null) {
                    fragmentTransaction.show(fragment_activity);
                } else {
                    fragment_activity = new ActivitiesFragment();
                    fragmentTransaction.add(R.id.frame_content, fragment_activity);
                }

                break;
            case 1:// 发现

                if (fragment_find != null) {
                    fragmentTransaction.show(fragment_find);
                } else {
                    fragment_find = new FindFragment();
                    fragmentTransaction.add(R.id.frame_content, fragment_find);
                }


                break;

            case 3:// 游戏王国

                if (fragment_kingdom != null) {
                    fragmentTransaction.show(fragment_kingdom);
                } else {
                    fragment_kingdom = new KingdomFragment();
                    fragmentTransaction.add(R.id.frame_content, fragment_kingdom);
                }

                break;
            case 5:// 我的
                if (fragment_bump != null) {
                    fragmentTransaction.show(fragment_bump);
                } else {
                    fragment_bump = new UserCenterFragment();
                    fragmentTransaction.add(R.id.frame_content, fragment_bump);
                }

                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {

        if (fragment_activity != null) {
            transaction.hide(fragment_activity);
        }
        if (fragment_bump != null) {
            transaction.hide(fragment_bump);
        }
        if (fragment_nearby != null) {
            transaction.hide(fragment_nearby);
        }
        if (fragment_kingdom != null) {
            transaction.hide(fragment_kingdom);
        }
        if (fragment_find != null) {
            transaction.hide(fragment_find);
        }
    }


    /**
     * 获取版本信息
     */
    private void getVersionInfo() {

        GainVersionInfoService gainVersionInfoService = new GainVersionInfoService(this);
        gainVersionInfoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                GainVersionInfoRespBean gainVersionInfoRespBean = (GainVersionInfoRespBean) respBean;
                VersionChkDTO dto = gainVersionInfoRespBean.getResp();

                if (dto != null) {
                    YpSettings.gVersionChkDTO = dto;
                    CommonObservable.getInstance().notifyObservers(CommonObserver.NewVersionObserver.class);

                } else {

                    addDillyTask();
                }
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                addDillyTask();
            }
        });

        gainVersionInfoService.enqueue();
    }

    /**
     * 检查版本
     */
    private void dealWithVersionDto() {


        contentView.post(new Runnable() {

            @Override
            public void run() {


                try {

                    checkVerDTO = YpSettings.gVersionChkDTO;
                    if (checkVerDTO != null) {

                        checkVersion(checkVerDTO);
                        // Settings.gVersionChkDTO = null;
                    } else {


                        addDillyTask();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    addDillyTask();

                }
            }
        });
    }

    boolean isOptionUpdate;

    /**
     * 检查版本处理
     */

    private synchronized void checkVersion(final VersionChkDTO dto) {


        try {

            packageManager = this.getPackageManager();
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            String cur_code = getVersionString(version);
            String lastest_code = getVersionString(dto.getLatestVersion());

            String min_code = getVersionString(dto.getMinimalSupportVersion());

            int cur_version_code = Integer.valueOf(cur_code);
            int min_version_code = Integer.valueOf(min_code);
            int lastest_version_code = Integer.valueOf(lastest_code);

            // 当前版本小于最新版本
            if (cur_version_code < lastest_version_code) {
                // 判断当前版本是否高于最低支持版本
                if (cur_version_code >= min_version_code) {
                    // 选择升级
                    //					showNormalVersionDialog(dto);
                    if (SharedprefUtil.getBoolean(this, "option_update", true)) {
                        SharedprefUtil.saveBoolean(this, "option_update", false);
                        isOptionUpdate = true;
                        DownloadLink = dto.getDownloadLink();
                        versiondialog = DialogUtil.createHintOperateDialog(IndexActivity.this, "版本升级", dto.getReleaseNotes(), "取消", "立即升级", VerbackCallListener);
                        versiondialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                        versiondialog.setCancelable(false);
                        if (!isFinishing()) {
                            versiondialog.show();
                        }
                    } else {
                        addDillyTask();
                    }

                } else {
                    // 强制升级

                    DownloadLink = dto.getDownloadLink();
                    versiondialog = DialogUtil.createHintOperateDialog(IndexActivity.this, "当前版本过低,强制升级", dto.getReleaseNotes(), "退出", "立即升级", VerbackCallListener);
                    versiondialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                    versiondialog.setCancelable(false);
                    if (!isFinishing()) {
                        versiondialog.show();
                    }

                }
            } else {

                addDillyTask();
            }

        } catch (NameNotFoundException e) {
            e.printStackTrace();


            addDillyTask();

        }

    }

    private BackCallListener VerbackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                versiondialog.dismiss();
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(DownloadLink);
            intent.setData(content_url);
            IndexActivity.this.startActivity(intent);
            finish();
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                versiondialog.dismiss();
            }
            if (isOptionUpdate) {
                addDillyTask();
                return;
            }
            finish();
        }
    };

    public static String getVersionString(String str) {
        // 先定义一个集合来存放分解后的字符
        List<String> list = new ArrayList<String>();
        String streee = "";

        for (int i = 0; i < str.length(); i++) {
            streee = str.substring(i, i + 1);
            list.add(streee);
        }

        // 定义一个存放最终字符串的StringBuffer
        StringBuffer strb = new StringBuffer();

        for (int j = 0; j < list.size(); j++) {
            String a = list.get(j).toString();
            // 如果不是？号就把这个字符加在上面定义的StringBuffer
            if (!a.equals(".")) {
                strb.append(a);
            }
        }
        return strb.toString();
    }


    /**
     * 退出
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                DialogUtil.showDisCoverNetToast(IndexActivity.this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     *
     */

    private void getSync() {

        UserSyncBean bumpUserSyncBean = new UserSyncBean();
        bumpUserSyncBean.setUserid(userid);
        UserSyncService bumpUserSyncService = new UserSyncService(this);
        bumpUserSyncService.parameter(bumpUserSyncBean);
        bumpUserSyncService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UserSyncRespBean bumpUserSyncRespBean = (UserSyncRespBean) respBean;
                SyncDto dto = bumpUserSyncRespBean.getResp();
                try {
                    if (null != dto) {

                        int total = dto.getVisits().getTotal();
                        int newadd = dto.getVisits().getNewAdded();

                        if (newadd != 0) {
                            icon_look_me_iv.setVisibility(View.VISIBLE);
                            LoginVideoStatusDto loginVideoStatusDto = DbHelperUtils.getLoginVideoStatusDto(userid);
                            if (loginVideoStatusDto == null) {
                                loginVideoStatusDto = new LoginVideoStatusDto();
                                loginVideoStatusDto.setId(userid);
                                loginVideoStatusDto.setVideoVerificationStatus(dto.getVideoVerificationStatus());
                                if (dto.getVideoVerificationStatus() == 0 || dto.getVideoVerificationStatus() == 3) {
                                    loginVideoStatusDto.setIsVisible(1);
                                } else {
                                    loginVideoStatusDto.setIsVisible(0);
                                }
                                App.getInstance().db.save(loginVideoStatusDto);
                            } else {
                                if (loginVideoStatusDto.getVideoVerificationStatus() != dto.getVideoVerificationStatus()) {
                                    loginVideoStatusDto.setVideoVerificationStatus(dto.getVideoVerificationStatus());
                                    loginVideoStatusDto.setIsVisible(1);
                                    App.getInstance().db.update(loginVideoStatusDto);
                                }
                            }

                        } else {
                            LoginVideoStatusDto loginVideoStatusDto = DbHelperUtils.getLoginVideoStatusDto(userid);
                            if (loginVideoStatusDto == null) {
                                loginVideoStatusDto = new LoginVideoStatusDto();
                                loginVideoStatusDto.setId(userid);
                                loginVideoStatusDto.setVideoVerificationStatus(dto.getVideoVerificationStatus());
                                if (dto.getVideoVerificationStatus() == 0 || dto.getVideoVerificationStatus() == 3) {
                                    icon_look_me_iv.setVisibility(View.VISIBLE);
                                    loginVideoStatusDto.setIsVisible(1);
                                } else {
                                    long noReadnum = ChatUtils.getNoReadNum(userid + "");
                                    if (noReadnum > 0) {
                                        icon_look_me_iv.setVisibility(View.VISIBLE);
                                    } else {
                                        boolean prize = SharedprefUtil.getBoolean(IndexActivity.this, userid + "" + "prize", false);

                                        if (prize) {
                                            icon_look_me_iv.setVisibility(View.VISIBLE);
                                        } else {
                                            icon_look_me_iv.setVisibility(View.GONE);
                                        }
                                    }
                                    loginVideoStatusDto.setIsVisible(0);
                                }
                                App.getInstance().db.save(loginVideoStatusDto);
                            } else {
                                if (loginVideoStatusDto.getVideoVerificationStatus() == dto.getVideoVerificationStatus()) {
                                    if (loginVideoStatusDto.getIsVisible() == 1) {
                                        icon_look_me_iv.setVisibility(View.VISIBLE);
                                    } else {
                                        long noReadnum = ChatUtils.getNoReadNum(userid + "");
                                        if (noReadnum > 0) {
                                            icon_look_me_iv.setVisibility(View.VISIBLE);
                                        } else {
                                            boolean prize = SharedprefUtil.getBoolean(IndexActivity.this, userid + "" + "prize", false);

                                            if (prize) {
                                                icon_look_me_iv.setVisibility(View.VISIBLE);
                                            } else {
                                                icon_look_me_iv.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                } else {
                                    icon_look_me_iv.setVisibility(View.VISIBLE);
                                    loginVideoStatusDto.setVideoVerificationStatus(dto.getVideoVerificationStatus());
                                    loginVideoStatusDto.setIsVisible(1);
                                    App.getInstance().db.update(loginVideoStatusDto);
                                }
                            }

                        }

                        DbHelperUtils.saveOrupdateVisits(userid, total, newadd);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new OnCallBackFailListener());

        bumpUserSyncService.enqueue();

    }


    /***
     * 获取用户信息及相册信息
     */
    private void getUserInfo() {

        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUserId(userid);
        userInfoBean.setNewAlbum(true);
        userInfoBean.setBubble(true);
        userInfoBean.setDating(true);
        userInfoBean.setWish(true);
        userInfoBean.setVerification(true);
        userInfoBean.setFriends(true);

        UserInfoService userInfoService = new UserInfoService(this);
        userInfoService.parameter(userInfoBean);
        userInfoService.callBack(new OnCallBackSuccessListener() {
                                     @Override
                                     public void onSuccess(RespBean respBean) {
                                         super.onSuccess(respBean);

                                         UserInfoRespBean userInfoRespBean = (UserInfoRespBean) respBean;
                                         UserDto userInfo = userInfoRespBean.getResp();
                                         String jsonstr = JsonUtils.toJson(userInfo);
                                         if (null != userInfo) {
                                             // 保存数据
                                             DbHelperUtils.saveUserInfo(userid, jsonstr);
                                             // 保存数据
                                             DbHelperUtils.saveBaseUser(userid, userInfo);
                                         }
                                         if (loadingDiaog != null) {
                                             loadingDiaog.dismiss();
                                             find_layout_indexActivity.performClick();
                                         }


                                     }
                                 }, new OnCallBackFailListener() {
                                     @Override
                                     public void onFail(RespBean respBean) {
                                         super.onFail(respBean);
                                         if (loadingDiaog != null) {
                                             loadingDiaog.dismiss();
                                             find_layout_indexActivity.performClick();
                                         }
                                     }
                                 }
        );
        userInfoService.enqueue();
    }


    /**
     * 获取网咯上的每日任务，并加入到本地任务中
     */
    private void addDillyTask() {

        // 每日首次报到
        DaillyTask.getInstance().enqueue(IndexActivity.this, new OnDaillTaskListener() {
            @Override
            public void onSuccess() {

                /**
                 * 每日钥匙
                 */
                int selected_tab_Id = SharedprefUtil.getInt(IndexActivity.this, YpSettings.DISCOVER_TAB_ID, 0);
                getDaillyTaskKeys(selected_menu_type, selected_tab_Id);

            }
        });


    }

    /**
     * 获取本地的每日任务
     *
     * @param selectedTypeId
     * @param selectedTabId
     */
    private void getDaillyTaskKeys(int selectedTypeId, int selectedTabId) {


        if ((1 == selectedTypeId || 0 == selectedTypeId) && 0 == selectedTabId) {


            DaillyTaskService.getInstance().seekTask();

            DaillyTaskService.getInstance().getNowTask(new OnDaillyTaskServiceListener() {
                @Override
                public boolean execution(String data) {


                    Dialog dialog = DialogUtil.createDailyhintDialog(IndexActivity.this, data);
                    dialog.show();
                    return true;
                }
            }, YpSettings.DAILLY_TASK_KEYS);


        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("SyncVideoStateEvent")

            }
    )
    public void syncVideoStateEvent(SyncVideoStateEvent event) {
        Visits dto = DbHelperUtils.getVisits(userid);

        if (dto != null && dto.getNewadded() != 0) {

            icon_look_me_iv.setVisibility(View.VISIBLE);

        } else {
            LoginVideoStatusDto loginVideoStatusDto = DbHelperUtils.getLoginVideoStatusDto(userid);
            if (loginVideoStatusDto == null) {

                icon_look_me_iv.setVisibility(View.VISIBLE);

            } else {
                if (loginVideoStatusDto.getIsVisible() == 1) {
                    icon_look_me_iv.setVisibility(View.VISIBLE);
                } else {
                    long noReadnum = ChatUtils.getNoReadNum(userid + "");
                    if (noReadnum > 0) {
                        icon_look_me_iv.setVisibility(View.VISIBLE);
                    } else {

                        boolean prize = SharedprefUtil.getBoolean(IndexActivity.this, userid + "" + "prize", false);

                        if (prize) {
                            icon_look_me_iv.setVisibility(View.VISIBLE);
                        } else {
                            icon_look_me_iv.setVisibility(View.GONE);
                        }

                    }
                }
            }
        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("ReceiveNewMessage")

            }
    )
    public void ReceiveNewMessage(CommonEvent event) {
        getSync();
    }


    /**
     * 获取版本信息
     */
    private void getOAuthToken() {

        OAuthTokenService oAuthTokenService = new OAuthTokenService(this);
        oAuthTokenService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                OAuthTokenRespBean oAuthTokenRespBean = (OAuthTokenRespBean) respBean;
                OAuthTokenRespBean.OAuthTokenDto dto = oAuthTokenRespBean.getResp();

                RxBus.get().post("GameTokenEvent", new GameTokenEvent(dto.getAccessToken(), dto.getRefreshToken(), dto.getUnionId()));

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

            }
        });

        oAuthTokenService.enqueue();
    }


    private void setConnectionListener() {

        //设置网络连接监听器，连接建立／断开时回调
        TIMManager.getInstance().setConnectionListener(new TIMConnListener() {//连接监听器
            @Override
            public void onConnected() {//连接建立

                if (YpSettings.isTest) {
                    LoginBusiness.loginIm(LoginUser.getInstance().getUserId() + "@test", LoginUser.getInstance().getUserSig(), IndexActivity.this);
                } else {
                    LoginBusiness.loginIm(LoginUser.getInstance().getUserId() + "", LoginUser.getInstance().getUserSig(), IndexActivity.this);
                }


            }

            @Override
            public void onDisconnected(int code, String desc) {//连接断开
                //接口返回了错误码code和错误描述desc，可用于定位连接断开原因
                //错误码code含义请参见错误码表
                RxBus.get().post("onConnectionListenerTitle", new CommonEvent<>(0));
            }

            @Override
            public void onWifiNeedAuth(String s) {

            }
        });

    }


    @Override
    public void onError(int i, String s) {

        RxBus.get().post("onConnectionListenerTitle", new CommonEvent<>(0));

        if (i == 6023 || i == 6206 || i == 6207 || i == 6208) {

            if (i == 6208) {
                DialogUtil.showDisCoverNetToast(IndexActivity.this, getString(R.string.kick_logout));
            } else {
                DialogUtil.showDisCoverNetToast(IndexActivity.this, getString(R.string.login_error));
            }

            ActivityUtil.jump(IndexActivity.this, SelectEntryActivity.class, null, 2, 100);

        }

    }

    @Override
    public void onSuccess() {

        RxBus.get().post("onConnectionListenerTitle", new CommonEvent<>(1));

    }


}
