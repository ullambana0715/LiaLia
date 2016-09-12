//package cn.chono.yopper.smack;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import org.jivesoftware.smack.ConnectionListener;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smack.packet.StreamError;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import cn.chono.yopper.base.App;
//import cn.chono.yopper.common.YpSettings;
//import cn.chono.yopper.smack.service.XXService;
//import cn.chono.yopper.task.RemoveAliasAsyncTask;
//import cn.chono.yopper.utils.ContextUtil;
//import cn.chono.yopper.utils.SHA;
//
//
//
//public class XmppConnectionStateListence implements ConnectionListener {
//
//	private XXService mService;// 主服务
//	private Timer tExit;
//	private int logintime = 1000;
//
//	public XmppConnectionStateListence(XXService service) {
//
//		this.mService = service;
//
//	}
//
//	@Override
//	public void connectionClosed() {
//
//	}
//
//	@Override
//	public void connectionClosedOnError(Exception e) {
//
//
//		XMPPException xmppEx = (XMPPException) e;
//		StreamError error = xmppEx.getStreamError();
//
//		//被挤掉线
//		if (error.getCode().equals("conflict")) {
//
//			if(LoginUser.getInstance().getUserId()!=0){
//				RemoveAliasAsyncTask loginAsyncTask = new RemoveAliasAsyncTask();
//				loginAsyncTask.execute("账号在另外一台设备登录了");
//			}
//		}else{
//			Intent intent = new Intent();
//			intent.setAction("cn.yopper.chono.connect.state");
//			Bundle bundle=new Bundle();
//			bundle.putString(YpSettings.CONNECT_STATE, "消息（未连接）");
//			intent.putExtras(bundle);
//			ContextUtil.getContext().sendBroadcast(intent);
//
//			if(tExit!=null){
//				tExit.schedule(new timetask(), logintime);
//			}else{
//				tExit = new Timer();
//				tExit.schedule(new timetask(), logintime);
//			}
//		}
//		//
//
//	}
//
//	@Override
//	public void reconnectingIn(int arg0) {
//
//	}
//
//	@Override
//	public void reconnectionFailed(Exception arg0) {
//
//		Intent intent = new Intent();
//		intent.setAction("cn.yopper.chono.connect.state");
//		Bundle bundle=new Bundle();
//		bundle.putString(YpSettings.CONNECT_STATE, "消息（未连接）");
//		intent.putExtras(bundle);
//		ContextUtil.getContext().sendBroadcast(intent);
//
//		if(tExit!=null){
//			tExit.schedule(new timetask(), logintime);
//		}else{
//			tExit = new Timer();
//			tExit.schedule(new timetask(), logintime);
//		}
//
//
//	}
//
//	@Override
//	public void reconnectionSuccessful() {
//
//	}
//
//
//
//	class timetask extends TimerTask {
//		@Override
//		public void run() {
//
//			String account  = App.loginUser.getUserId() + "";
//			String password = App.loginUser.getAuthToken();
//
//			if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)){
//				// 如果没有帐号，也直接返回
//				return;
//			}
//			mService.login(account, SHA.encodeByMD5(password));// 重连
//		}
//	}
//
//
//}
