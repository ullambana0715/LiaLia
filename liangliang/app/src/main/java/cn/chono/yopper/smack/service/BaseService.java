//package cn.chono.yopper.smack.service;
//
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.PowerManager;
//import android.os.PowerManager.WakeLock;
//import android.os.Vibrator;
//import android.widget.RemoteViews;
//
//import com.lidroid.xutils.util.LogUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import cn.chono.yopper.R;
//import cn.chono.yopper.YpSettings;
//import cn.chono.yopper.activity.base.IndexActivity;
//import cn.chono.yopper.utils.CheckUtil;
//import cn.chono.yopper.utils.Constants;
//import cn.chono.yopper.utils.SharedprefUtil;
//
//
//public abstract class BaseService extends Service {
//
//    private static final String APP_NAME = "xx";
//    private static final int MAX_TICKER_MSG_LEN = 50;
//    protected static int SERVICE_NOTIFICATION = 1;
//
//    private NotificationManager mNotificationManager;
//    private Notification mNotification;
//    private Vibrator mVibrator;
//    protected WakeLock mWakeLock;
//
//    private Map<String, Integer> mNotificationCount = new HashMap<String, Integer>(2);
//    private Map<String, Integer> mNotificationId = new HashMap<String, Integer>(2);
//    private int mLastNotificationId = 2;
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
//    }
//
//    @Override
//    public void onRebind(Intent intent) {
//        super.onRebind(intent);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APP_NAME);
//        addNotificationMGR();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    private void addNotificationMGR() {
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//    }
//
//    protected void notifyClient(String fromJid, String fromUserName, String message, boolean showNotification) {
//        if (!showNotification) {
//            return;
//        }
//        mWakeLock.acquire();
////        setNotification(fromJid, fromUserName, message);
//
//        int mNotificationCounter = 0;
//        if (mNotificationCount.containsKey(fromJid)) {
//            mNotificationCounter = mNotificationCount.get(fromJid);
//        }
//        mNotificationCounter++;
//        mNotificationCount.put(fromJid, mNotificationCounter);
//
//
//        int notifyId = 0;
//        if (mNotificationId.containsKey(fromJid)) {
//            notifyId = mNotificationId.get(fromJid);
//        } else {
//            mLastNotificationId++;
//            notifyId = mLastNotificationId;
//            mNotificationId.put(fromJid, Integer.valueOf(notifyId));
//        }
//
//
//        boolean vibraNotify = SharedprefUtil.getBoolean(this, Constants.VIBRATIONNOTIFY, true);
//        if (vibraNotify) {
//            mVibrator.vibrate(400);
//        }
//
//
//        Intent mNotificationIntent = new Intent(this, IndexActivity.class);
//
//        mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, mNotificationIntent, 0);
//        // 通过Notification.Builder来创建通知，注意API Level
//        // API11之后才支持
//        Notification notify2 = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.notification_icon) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
//                // icon)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.application_icon))
//                .setTicker("俩俩:" + "收到一条消息！")// 设置在status
//                // bar上显示的提示文字
//                .setContentTitle("俩俩")// 设置在下拉status
//                // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
//                .setContentText("收到一条消息")// TextView中显示的详细内容
//                .setContentIntent(pendingIntent2) // 关联PendingIntent
////                .setNumber(mNotificationCounter) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
//                .getNotification(); // 需要注意build()是在API level
//        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
//        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
//        mNotificationManager.notify(notifyId, notify2);
//
//        mWakeLock.release();
//    }
//
//
//
//
//    private void setNotification(String fromJid, String fromUserId, String message) {
//
////        int mNotificationCounter = 0;
////        if (mNotificationCount.containsKey(fromJid)) {
////            mNotificationCounter = mNotificationCount.get(fromJid);
////        }
////        mNotificationCounter++;
////        mNotificationCount.put(fromJid, mNotificationCounter);
//
////        mNotification = new Notification(R.drawable.application_icon, "收到一条消息", System.currentTimeMillis());
////        Uri userNameUri = Uri.parse(fromJid);
////
////
////        mNotificationIntent.setData(userNameUri);
////
////        Bundle bundle = new Bundle();
////
////        String userid = fromJid.substring(0, fromJid.indexOf("@"));
////        if (CheckUtil.isNumeric(userid)) {
////            bundle.putInt(YpSettings.USERID, Integer.valueOf(userid));
////        }
////        mNotificationIntent.putExtras(bundle);
//
////        mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
////
////        if (Build.VERSION.SDK_INT >= 16) {
////            mNotification = new Notification.Builder(this).setContentTitle("俩俩").setContentText("收到一条消息").setContentIntent(pendingIntent).build();
////        } else {
////            mNotification.setLatestEventInfo(this, "俩俩", "收到一条消息", pendingIntent);
////        }
////
////        if (mNotificationCounter > 1)
////            mNotification.number = mNotificationCounter;
////        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
////        LogUtils.e("自定义通知====");
//
//    }
//
//
//    public void clearNotification(String Jid) {
//        int notifyId = 0;
//        if (mNotificationId.containsKey(Jid)) {
//            notifyId = mNotificationId.get(Jid);
//            mNotificationManager.cancel(notifyId);
//        }
//    }
//
//
//}
