package cn.chono.yopper.utils;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.lidroid.xutils.util.LogUtils;

public class ISO8601 {
	/** Transform Calendar to ISO 8601 string. */
	public static String fromCalendar(final Calendar calendar) {
		Date date = calendar.getTime();
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	/** Get current date and time formatted as ISO 8601 string. */
	public static String now() {
		return fromCalendar(GregorianCalendar.getInstance());
	}

	// 2015-05-21T17:02:24.771+08:00

	/** Transform ISO 8601 string to Calendar. */
	@SuppressLint("SimpleDateFormat")
	public static Calendar toCalendar(final String iso8601string)
			throws ParseException {
		Calendar calendar = GregorianCalendar.getInstance();

		String[] datelist = iso8601string.split("T");

		String time = datelist[1].substring(0, 8);

		String s = datelist[0] + " " + time;
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
		calendar.setTime(date);
		return calendar;
	}

	public static long getTime(final String iso8601string) {
		Calendar calendar = null;
		if(CheckUtil.isEmpty(iso8601string)){
			return 0;
		}
		try {
			calendar = toCalendar(iso8601string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return 0;
		}
		return calendar.getTimeInMillis();

	}

	public static String encodeGB(String string) {
		// 转换中文编码
		String split[] = string.split("/");
		for (int i = 1; i < split.length; i++) {
			try {
				split[i] = URLEncoder.encode(split[i], "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			split[0] = split[0] + "/" + split[i];
		}
		split[0] = split[0].replaceAll("\\+", "%2B");// 处理空格
		split[0] = split[0].replaceAll("\\{", "%7B");
		split[0] = split[0].replaceAll("\\}", "%7D");
		split[0] = split[0].replaceAll("%3A", "\\:");
		return split[0];
	}

	public static String encodeURIComponent(String s) {
		String result;

		try {
			result = URLEncoder.encode(s, "UTF-8");
			
//			result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20")
//					.replaceAll("\\%21", "!").replaceAll("\\%27", "'")
//					.replaceAll("\\%28", "(").replaceAll("\\%29", ")")
//					.replaceAll("\\%7E", "~");
			
		} catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}


	public static String decodeURIComponent(String s) {
		String result;

		try {
			result = URLDecoder.decode(s, "UTF-8");

//			result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20")
//					.replaceAll("\\%21", "!").replaceAll("\\%27", "'")
//					.replaceAll("\\%28", "(").replaceAll("\\%29", ")")
//					.replaceAll("\\%7E", "~");

		} catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}
}
