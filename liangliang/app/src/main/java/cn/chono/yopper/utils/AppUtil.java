package cn.chono.yopper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author SQ
 *
 */
public class AppUtil {

	/**
	 * 获取唯一设备号
	 *
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 * @Title: getMacAddress
	 */
	@SuppressLint("NewApi")
	public static  String getMacAddress(Context context) {

		String ClientId = SharedprefUtil.get(context, "clientID", "");
		if (TextUtils.isEmpty(ClientId)) {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			final String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			ClientId = deviceUuid.toString();
			SharedprefUtil.save(context, "clientID", UUIDGenerator.getUUIDForm(ClientId));
		}
		return UUIDGenerator.getUUIDForm(ClientId);
	}


	/**
	 * 获取apk渠道号
	 *
	 * @param context
	 * @param channelKey
	 * @return
	 */
	public static String getChannelFromApk(Context context, String channelKey) {
		//从apk包中获取
		ApplicationInfo appinfo = context.getApplicationInfo();
		String sourceDir = appinfo.sourceDir;
		//注意这里：默认放在meta-inf/里， 所以需要再拼接一下
		String key = "META-INF/" + channelKey;
		String ret = "";
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(sourceDir);
			Enumeration<?> entries = zipfile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();
				if (entryName.startsWith(key)) {
					ret = entryName;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String[] split = ret.split("_");
		String channel = "";
		if (split != null && split.length >= 2) {
			channel = ret.substring(split[0].length() + 1);
		}
		return channel;
	}


	/**
	 * 获取手机版本信息
	 *
	 * @param context
	 * @return
	 */
	public static String getPhoneVersion(Context context) {
		String VERSION_RELEASE = SharedprefUtil.get(context, "phone_version", "");

		if (TextUtils.isEmpty(VERSION_RELEASE)) {
			VERSION_RELEASE = android.os.Build.VERSION.RELEASE;
			SharedprefUtil.save(context, "phone_version", VERSION_RELEASE);
		}

		return VERSION_RELEASE;
	}



	public static String getAppVersionName(Context context) {

		String versionName = null;

		PackageManager packageManager = context.getPackageManager();

		PackageInfo packInfo;

		try {

			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packInfo.versionName;

		} catch (PackageManager.NameNotFoundException e) {

			e.printStackTrace();
		}

		return versionName;
	}

	public static int getAppVersionCode(Context context) {

		int versionCode = 0;

		PackageManager packageManager = context.getPackageManager();

		PackageInfo packInfo;

		try {

			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionCode = packInfo.versionCode;

		} catch (PackageManager.NameNotFoundException e) {

			e.printStackTrace();
		}

		return versionCode;
	}




	/**
	 * 获取保存过期时间
	 */
	public static long getRefreshTokenExpiration(Context context) {
		return SharedprefUtil.getLong(context, "refreshTokenExpiration", 0);

	}

	/**
	 * 保存登陆成功后过期的时间
	 *
	 * @param context
	 * @param expiration
	 */
	public static void setRefreshTokenExpiration(Context context, long expiration) {

		long currentTime = TimeUtil.getCurrentTimeMillis();// 当前时间
		long totalTime = (long) (currentTime + expiration);// 过期前的时间
		// 保存过期时间
		SharedprefUtil.saveLong(context, "refreshTokenExpiration", totalTime);

	}


}
