//package cn.chono.yopper.smack.service;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.text.TextUtils;
//
//import java.util.ArrayList;
//
//import cn.chono.yopper.common.Constants;
//import cn.chono.yopper.utils.SharedprefUtil;
//
////BroadcastReceiver 是对发送出来的广播进行过滤接收并响应的一类组件
//public class XXBroadcastReceiver extends BroadcastReceiver {
//
//	// 开机广播
//	public static final String BOOT_COMPLETED_ACTION = "cn.chono.yopper.action.BOOT_COMPLETED";
//
//	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		String action = intent.getAction();
//
//		// 相当于系统广播的action，你的网络状态不变化的话是不发这个action的，你网络状态变化后便发送这个action，你的广播才才会接收到这个相同的action，所有便会收到广播
//		if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
//
//			if (mListeners.size() > 0)// 通知接口完成加载
//				for (EventHandler handler : mListeners) {
//					handler.onNetChange();
//				}
//
//		} else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {// Intent.ACTION_SHUTDOWN
//																		// 是关机
//			// 如果关机那么就挂掉服务
//			Intent xmppServiceIntent = new Intent(context, XXService.class);
//			context.stopService(xmppServiceIntent);
//
//		} else {
//
//			// 如果为空就开启服务
//			if (!TextUtils.isEmpty(SharedprefUtil.get(context, Constants.ACCOUNT, "")) && SharedprefUtil.getBoolean(context, Constants.AUTO_START,true)) {
//
//				Intent i = new Intent(context, XXService.class);
//				i.setAction(BOOT_COMPLETED_ACTION);
//				context.startService(i);
//
//			}
//		}
//	}
//
//	public static abstract interface EventHandler {
//
//		public abstract void onNetChange();
//	}
//}