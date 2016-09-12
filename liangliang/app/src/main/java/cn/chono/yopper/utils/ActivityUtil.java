package cn.chono.yopper.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import java.util.List;
import cn.chono.yopper.common.YpSettings;

/**
 *
 * @author zxb
 *
 */
public class ActivityUtil {
	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 *
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPenLocation(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}



	/**
	 * 窗体跳转
	 *
	 * @param old
	 * @param cls
	 */
	public static void jump(Context old, Class<?> cls, Bundle mBundle,
							int flagType, int animType) {

		Intent intent = new Intent();
		intent.setClass(old, cls);
		if (mBundle != null) {
			intent.putExtras(mBundle);
		}



		switch (flagType) {
			case 1:
				// ABCD D跳B 则剩AB
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case 2:
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
		}

		switch (animType) {
			case YpSettings.ACTIVITY_IN_RIGHT_OUT_LEFT_ANIM_TYPE:
				old.startActivity(intent);
//			ActivityUtil.overridePendingTransition(activity,
//					R.anim.in_from_right, R.anim.out_to_left);

				break;

			default:
				old.startActivity(intent);
				break;
		}

	}

	/**
	 *
	 * 窗体跳转 。可设置动画
	 *
	 *
	 * @param old
	 * @param cls
	 * @param mBundle
	 * @param enterAnim
	 * @param exitAnim
	 */

	public static void jump(Context old, Class<?> cls, Bundle mBundle,
							int flagType, int enterAnim, int exitAnim) {
		Intent intent = new Intent();
		intent.setClass(old, cls);
		if (mBundle != null) {
			intent.putExtras(mBundle);
		}



		switch (flagType) {
			// ABCD D跳B 则剩AB
			case 1:
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case 2:
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
		}

		old.startActivity(intent);
//		ActivityUtil.overridePendingTransition(activity, enterAnim, exitAnim);
	}

	public static void jumpForResult(Context old, Class<?> cls, Bundle mBundle,
									 int requestCode, int flagType, int animType) {
		Intent intent = new Intent();
		intent.setClass(old, cls);
		if (mBundle != null) {
			intent.putExtras(mBundle);
		}

		Activity activity = (Activity) old;
		switch (flagType) {
			// ABCD D跳B 则剩AB
			case 1:
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case 2:
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
		}

		switch (animType) {
			case YpSettings.ACTIVITY_IN_RIGHT_OUT_LEFT_ANIM_TYPE:
				activity.startActivityForResult(intent, requestCode);
//			ActivityUtil.overridePendingTransition(activity,
//					R.anim.in_from_right, R.anim.out_to_left);
				break;

			default:
				activity.startActivityForResult(intent, requestCode);
				break;
		}

	}

	public static void jumpForResult(Context old, Class<?> cls, Bundle mBundle,
									 int requestCode, int flagType, int enterAnim, int exitAnim) {
		Intent intent = new Intent();
		intent.setClass(old, cls);
		if (mBundle != null) {
			intent.putExtras(mBundle);
		}

		Activity activity = (Activity) old;
		switch (flagType) {
			// ABCD D跳B 则剩AB
			case 1:
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case 2:
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
		}
		activity.startActivityForResult(intent, requestCode);
//		ActivityUtil.overridePendingTransition(activity, enterAnim, exitAnim);
	}

	/**
	 * 获得应用是否在前台
	 */
	public static boolean isOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		if(activityManager==null){
			return false;
		}
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo != null && tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (context.getPackageName().equals(
					tasksInfo.get(0).topActivity.getPackageName())) {
				return true;
			}
		}
		return false;
	}



	/**
	 * 调用拍照程序拍摄图片，返回图片对应的Uri，应处理onActivityResult
	 * ContentResolver的insert方法会默认创建一张空图片，如取消了拍摄，应根据方法返回的Uri删除图片
	 *
	 * @param activity
	 * @param requestCode
	 * @param fileName
	 * @return
	 */
	// TODO
	public static Uri captureImage(Activity activity, int requestCode, String fileName, String desc) {
		// 设置文件参数
		// TODO
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION, desc);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		// 获得uri

		Uri imageUri = activity.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		activity.startActivityForResult(intent, requestCode);
		return imageUri;
	}

	/**
	 * 通过地址跳转到网页
	 *
	 * @param activity
	 * @param url
	 */
	public static void jumpToWeb(Activity activity, String url) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			activity.startActivity(intent);
			// ActivityUtil.overridePendingTransition(activity,
			// R.anim.right_slide_in, R.anim.right_slide_out);
		} catch (Exception e) {
			e.printStackTrace();
			// DialogUtil.showToast(activity, "抱歉，无法打开链接");
		}
	}



	public static String getTopActivity() {
		ActivityManager manager = (ActivityManager) ContextUtil.getContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return (runningTaskInfos.get(0).topActivity).toString();
		else
			return null;
	}


}
