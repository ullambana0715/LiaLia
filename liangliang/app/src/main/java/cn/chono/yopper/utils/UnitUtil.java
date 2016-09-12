package cn.chono.yopper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * dip 与像素的相互转换
 * 
 * @author sam.sun
 */
public class UnitUtil {

	// 下面两个带context的方法是为了向下兼容，不推荐使用
	/**
	 * @deprecated
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		return dip2px(dipValue,context);
	}

	/**
	 * @deprecated
	 * @param context
	 * @return
	 */
	public static float px2dip(Context context, int pxValue) {
		return px2dip(pxValue,context);
	}

	/**
	 * 将dip转换为像素
	 * 
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue,Context context) {
		float scale =context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将像素转换为dip
	 * 
	 * @param pxvalue
	 * @return
	 */
	public static float px2dip(int pxvalue,Context context) {
		float scale = context.getResources().getDisplayMetrics().density;
		return pxvalue / scale;
	}

	/**
	 * 获取屏宽度像素数
	 * 
	 * @return
	 */
	public static int getScreenWidthPixels(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度像素数
	 * 
	 * @return
	 */

	public static int getScreenHeightPixels(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取手机密度
	 * 
	 * @return
	 */

	public static float getScreenDIP(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}


	/**
	 * 获取屏幕底部虚拟按键的高度
	 *
	 * @return
	 */
	public static int getNavigationBarHeight(Context context) {
		int navigationBarHeight = 0;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if (id > 0 && checkDeviceHasNavigationBar(context)) {
			navigationBarHeight = rs.getDimensionPixelSize(id);
		}
		return navigationBarHeight;
	}

	/**
	 * 判断是否存在底部虚拟按键
	 *
	 * @return
	 */
	public static boolean checkDeviceHasNavigationBar(Context context) {
		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs
				.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasNavigationBar = rs.getBoolean(id);
		}
		try {
			Class systemPropertiesClass = Class
					.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass,
					"qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (Exception e) {
		}

		return hasNavigationBar;

	}

	/**
	 * 获取顶部状态栏的高度
	 *
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			return context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
			return 75;
		}
	}

	/**
	 * 获取屏幕大小
	 *
	 * @return
	 */
	public static int getScreentHeight(Activity activity) {
		int heightPixels;
		WindowManager w = activity.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		heightPixels = metrics.heightPixels;
		// includes window decorations (statusbar bar/navigation bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				heightPixels = (Integer) Display.class
						.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
			}
			// includes window decorations (statusbar bar/navigation bar)
		else if (Build.VERSION.SDK_INT >= 17)
			try {
				android.graphics.Point realSize = new android.graphics.Point();
				Display.class.getMethod("getRealSize",
						android.graphics.Point.class).invoke(d, realSize);
				heightPixels = realSize.y;
			} catch (Exception ignored) {
			}
		return heightPixels;
	}

}
