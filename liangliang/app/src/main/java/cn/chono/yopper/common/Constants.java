package cn.chono.yopper.common;

import android.os.Environment;

public class Constants {
	// 语音存储路径
	public static String RECORD_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/chat/record";
	public static String FILE_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/chat/file";

	public static final String LOGIN_ACTION = "cn.chono.action.LOGIN";

	public static final String GMAIL_SERVER = "";

	public final static String Server = "server";
	public final static String AUTO_START = "auto_start";

	public final static String AVAILABLE = "available";

	public final static String RESSOURCE = "account_resource";
	public final static String PRIORITY = "account_prio";
	public final static String CONN_STARTUP = "connstartup";
	public final static String AUTO_RECONNECT = "reconnect";
	public final static String MESSAGE_CARBONS = "carbons";
	public final static String LEDNOTIFY = "led";
	public final static String VIBRATIONNOTIFY = "vibration_list";
	public final static String SCLIENTNOTIFY = "ringtone";

	public final static String TICKER = "ticker";

	public final static String FOREGROUND = "foregroundService";
	public static final String INTENT_EXTRA_USERNAME = "username";// 昵称对应的key
	public final static String STATUS_MODE = "status_mode";
	public final static String STATUS_MESSAGE = "status_message";

	public final static String ACCOUNT = "account";
	public final static String PASSWORD = "password";
	public final static String MJD = "mjd";
	
	//登陆成功后，保存的字段
	public final static String userId="userId";
	public final static String refreshToken="refreshToken";
	public final static String authToken="authToken";
	public final static String expiration="expiration";


	public static final int HTTP_CONNECT_TIMEOUT = 16000;

}
