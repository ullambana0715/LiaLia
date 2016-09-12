//package cn.chono.yopper.smack.service;
//
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningAppProcessInfo;
//import android.app.ActivityManager.RunningTaskInfo;
//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.MediaPlayer;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.text.TextUtils;
//
//import com.lidroid.xutils.util.LogUtils;
//
//import java.util.HashSet;
//import java.util.List;
//
//import cn.chono.yopper.MainFrameActivity;
//import cn.chono.yopper.MainFrameActivity.BackPressHandler;
//import cn.chono.yopper.R;
//import cn.chono.yopper.YPApplication;
//import cn.chono.yopper.YpSettings;
//import cn.chono.yopper.activity.login.LoginOrRegisterActivity;
//import cn.chono.yopper.smack.SmackImpl;
//import cn.chono.yopper.smack.entity.ChatDto;
//import cn.chono.yopper.smack.service.XXBroadcastReceiver.EventHandler;
//import cn.chono.yopper.utils.ActivityUtil;
//import cn.chono.yopper.utils.ChatUtils;
//import cn.chono.yopper.utils.Constants;
//import cn.chono.yopper.utils.ContextUtil;
//import cn.chono.yopper.utils.PlaySoundPool;
//import cn.chono.yopper.utils.SHA;
//import cn.chono.yopper.utils.SharedprefUtil;
//import cn.chono.yopper.utils.VibratorUtil;
//
//public class XXService extends BaseService implements EventHandler, BackPressHandler {
//    public static final int CONNECTED = 0;
//    public static final int DISCONNECTED = -1;
//    public static final int CONNECTING = 1;
//    public static final String PONG_TIMEOUT = "pong timeout";// 连接超时
//    public static final String NETWORK_ERROR = "network error";// 网络错误
//    public static final String LOGOUT = "logout";// 手动退出
//    public static final String LOGIN_FAILED = "login failed";// 登录失败
//    public static final String DISCONNECTED_WITHOUT_WARNING = "disconnected without warning";// 没有警告的断开连接
//
//    private IBinder mBinder = new XXBinder();
//    private IConnectionStatusCallback mConnectionStatusCallback;
//    private SmackImpl mSmackable;
//    private Thread mConnectingThread;
//    private Handler mMainHandler = new Handler();
//
//    private boolean mIsFirstLoginAction;
//
//    // 自动重连 start
//    private static final int RECONNECT_AFTER = 2;
//    private static final int RECONNECT_MAXIMUM = 1 * 60;// 最大重连时间间隔
//    private static final String RECONNECT_ALARM = "cn.chono.yoppercom.way.xx.RECONNECT_ALARM";
//    // private boolean mIsNeedReConnection = false; // 是否需要重连
//    private int mConnectedState = DISCONNECTED; // 是否已经连接
//    private int mReconnectTimeout = RECONNECT_AFTER;
//    private Intent mAlarmIntent = new Intent(RECONNECT_ALARM);
//    private PendingIntent mPAlarmIntent;
//    private BroadcastReceiver mAlarmReceiver = new ReconnectAlarmReceiver();
//    // 自动重连 end
//    private ActivityManager mActivityManager;
//    private HashSet<String> mIsBoundTo = new HashSet<String>();// 用来保存当前正在聊天对象的数组
//
//    private LogoutReceiver logoutReceiver;
//
//    /**
//     * 注册注解面和聊天界面时连接状态变化回调
//     *
//     * @param cb
//     */
//    public void registerConnectionStatusCallback(IConnectionStatusCallback cb) {
//        mConnectionStatusCallback = cb;
//    }
//
//    public void unRegisterConnectionStatusCallback() {
//        mConnectionStatusCallback = null;
//    }
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        String chatPartner = intent.getDataString();
//        if ((chatPartner != null)) {
//            mIsBoundTo.add(chatPartner);
//        }
//        String action = intent.getAction();
//        if (!TextUtils.isEmpty(action) && TextUtils.equals(action, Constants.LOGIN_ACTION)) {
//            mIsFirstLoginAction = true;
//        } else {
//            mIsFirstLoginAction = false;
//        }
////		YpSettings.xxservice=((XXService.XXBinder) mBinder).getService();
//        return mBinder;
//    }
//
//    @Override
//    public void onRebind(Intent intent) {
//        super.onRebind(intent);
//        String chatPartner = intent.getDataString();
//        if ((chatPartner != null)) {
//            mIsBoundTo.add(chatPartner);
//        }
//        String action = intent.getAction();
//        if (!TextUtils.isEmpty(action) && TextUtils.equals(action, Constants.LOGIN_ACTION)) {
//            mIsFirstLoginAction = true;
//        } else {
//            mIsFirstLoginAction = false;
//        }
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        String chatPartner = intent.getDataString();
//        if ((chatPartner != null)) {
//            mIsBoundTo.remove(chatPartner);
//        }
//        return true;
//    }
//
//    public class XXBinder extends Binder {
//        public XXService getService() {
//            return XXService.this;
//        }
//    }
//
//
//
//
//    // 收到新消息
//    public void newMessage(final ChatDto dto) {
//        mMainHandler.post(new Runnable() {
//            public void run() {
//
//                Intent intent = new Intent();
//
//                intent.setAction("cn.yopper.chono.set.newmsg");
//
//                ContextUtil.getContext().sendBroadcast(intent);
//
//                if (dto.getJid().equals("system_dailly_match")) {
//
//                    VibratorUtil.Vibrate(1000);
//                    if (mConnectionStatusCallback != null) {
//                        mConnectionStatusCallback.ReceiveNewMsg(dto);
//                    }
//
//                } else {
//
//                    if (mConnectionStatusCallback != null) {
//                        mConnectionStatusCallback.ReceiveNewMsg(dto);
//                    }
//
//                    if (!isAppOnForeground()) {
//                        if (mSmackable != null) {
//                            notifyClient(dto.getJid(), "", dto.getMessage(), !mIsBoundTo.contains(dto.getJid()));
//                        }
//
//                    } else {
//
//                        if (SharedprefUtil.getBoolean(ContextUtil.getContext(), YpSettings.IS_OPEAN_MESSAGE_VOICE, true)) {
//                            long pre_time = SharedprefUtil.getLong(ContextUtil.getContext(), YpSettings.message_voice_time, 0);
//                            if (pre_time == 0) {
//                                MediaPlayer.create(ContextUtil.getContext(), R.raw.office).start();
//                                PlaySoundPool playSoundPool = new PlaySoundPool(ContextUtil.getContext());
//                                playSoundPool.loadSfx(R.raw.office, 1);
//                                playSoundPool.play(1, 0);
//                                SharedprefUtil.saveLong(ContextUtil.getContext(), YpSettings.message_voice_time, System.currentTimeMillis());
//                            } else {
//                                long cur_time = System.currentTimeMillis();
//                                if (cur_time - pre_time >= 2000) {
//                                    MediaPlayer.create(ContextUtil.getContext(), R.raw.office).start();
//                                    PlaySoundPool playSoundPool = new PlaySoundPool(ContextUtil.getContext());
//                                    playSoundPool.loadSfx(R.raw.office, 1);
//                                    playSoundPool.play(1, 0);
//                                    SharedprefUtil.saveLong(ContextUtil.getContext(), YpSettings.message_voice_time, cur_time);
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//
//
//            //}
//        });
//    }
//
//
//    /**
//     * 更新通知栏
//     *
//     * @param message
//     */
//    public void updateServiceNotification(String message) {
//        if (!SharedprefUtil.getBoolean(ContextUtil.getContext(), Constants.FOREGROUND, true))
//            return;
//        String title = SharedprefUtil.get(ContextUtil.getContext(), Constants.ACCOUNT, "");
//        Notification n = new Notification(R.drawable.application_icon, title, System.currentTimeMillis());
//        n.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
//
//        Intent notificationIntent = new Intent(this, LoginOrRegisterActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        n.contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        n.setLatestEventInfo(this, title, message, n.contentIntent);
//        startForeground(SERVICE_NOTIFICATION, n);
//    }
//
//
//
//
//
//
//    // 自动重连广播
//    private class ReconnectAlarmReceiver extends BroadcastReceiver {
//        public void onReceive(Context ctx, Intent i) {
//            if (!SharedprefUtil.getBoolean(XXService.this, Constants.AUTO_RECONNECT, true)) {
//                return;
//            }
//
//            if (mConnectedState != DISCONNECTED) {
//                return;
//            }
//
//            String account = YPApplication.loginUser.getUserId() + "";
//            String password = YPApplication.loginUser.getAuthToken();
//
//            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
//                return;
//            }
//            login(account, SHA.encodeByMD5(password));
//        }
//    }
//
//    public static final String LOGOUTACTION = "logout_action";
//
//    private class LogoutReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(LOGOUTACTION)) {
//                logout();
//            }
//        }
//    }
//
//    @Override
//    public void onNetChange() {
//        if (!ActivityUtil.isNetWorkAvailable(this)) {// 如果是网络断开，不作处理
//            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mReconnectTimeout * 1000, mPAlarmIntent);
//
//            connectionFailed(NETWORK_ERROR);
//            return;
//
//        }
//        if (isAuthenticated()) {// 如果已经连接上，直接返回
//            return;
//        }
//
//        String account = YPApplication.loginUser.getUserId() + "";
//        String password = YPApplication.loginUser.getAuthToken();
//
//        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
//            // 如果没有帐号，也直接返回
//            return;
//        }
//        login(account, SHA.encodeByMD5(password));// 重连
//    }
//
//
//
//
//}





