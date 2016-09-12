package cn.chono.yopper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cc on 16/5/12.
 */
public class TimeUtils {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }


    /**
     * date类型转换为String类型
     *
     * @param data       Date类型的时间
     * @param formatType 格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    /**
     * long类型转换为String类型
     *
     * @param currentTime 要转换的long类型的时间
     * @param formatType  要转换的string类型的时间格式
     * @return
     * @throws ParseException
     */
    public static String longToString(long currentTime, String formatType) {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }


    /**
     * string类型转换为date类型
     * strTime的时间格式必须要与formatType的时间格式相同
     *
     * @param strTime    要转换的string类型的时间
     * @param formatType 要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日HH时mm分ss秒，
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType) {

        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (Exception e) {

        }
        return date;
    }


    /**
     * long转换为Date类型
     *
     * @param currentTime 要转换的long类型的时间
     * @param formatType  要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return
     * @throws ParseException
     */
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }


    /**
     * string类型转换为long类型
     * strTime的时间格式和formatType的时间格式必须相同
     *
     * @param strTime    要转换的String类型的时间
     * @param formatType 时间格式
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * date类型转换为long类型
     * date要转换的date类型的时间
     */

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 标准时间yyyy-MM-dd'T'HH:mm:ss 转换为 long
     *
     * @param date
     * @return
     */
    public static long getFormat(String date) {
        long tiem = 0;
        try {
            tiem = stringToLong(date, "yyyy-MM-dd'T'HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tiem;

    }

    /**
     * long 时间转为 月 日 01-01
     *
     * @param time
     * @return
     */
    public static String getMMdd(long time) {

        Date date = new Date(time);

        Calendar cal = Calendar.getInstance();//使用日历类
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);//得到年
        int month = cal.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
        int day = cal.get(Calendar.DAY_OF_MONTH);//得到天
        int hour = cal.get(Calendar.HOUR_OF_DAY);//得到小时
        int minute = cal.get(Calendar.MINUTE);//得到分钟
        int second = cal.get(Calendar.SECOND);//得到秒

        return (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }

    /**
     * long 时间转为 时 分 08:01
     *
     * @param time
     * @return
     */

    public static String getHHmm(long time) {

        Date date = new Date(time);

        Calendar cal = Calendar.getInstance();//使用日历类
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);//得到年
        int month = cal.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
        int day = cal.get(Calendar.DAY_OF_MONTH);//得到天
        int hour = cal.get(Calendar.HOUR_OF_DAY);//得到小时
        int minute = cal.get(Calendar.MINUTE);//得到分钟
        int second = cal.get(Calendar.SECOND);//得到秒

        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);

    }

    /**
     * 指定日期获取星期几
     *
     * @param time
     * @return
     */
    public static String getDesignationDateWeek(long time) {


        String Week = "星期";
        Date date = new Date(time);

        Calendar cal = Calendar.getInstance();//使用日历类
        cal.setTime(date);


        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week += "天";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
            default:
                break;
        }
        return Week;
    }

    /**
     * 判断两个时间的相差多少秒
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static long getTimeDifference(long startTime, long endTime) {


        long time = (endTime - startTime) / (1000);


        return time;
    }

    /**
     * 秒转为时分秒。当时为0时，不返回时
     * 00时00分00秒
     * 00分00时
     *
     * @param second
     * @return
     */
    public static String getSecondTurnHMS(long second) {

        long h = 0;
        long d = 0;
        long s = 0;
        long temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }

        if (h == 0) {
            return (d < 10 ? "0" + d : d) + "分" + (s < 10 ? "0" + s : s) + "秒";
        }


        return (h < 10 ? "0" + h : h) + "时" + (d < 10 ? "0" + d : d) + "分" + (s < 10 ? "0" + s : s) + "秒";

    }


    /**
     * 获取两个时间相差的天数。以要比较的时间－当前时间＝相差的天数。结果为负时，则是昨天，等。为正时，则是明天，后天，等
     *
     * @param compareTime 要比较的时间
     * @param currentTime 当前时间
     * @return
     */
    public static int getApartDateDay(long compareTime, long currentTime) {

        int apartNumber = 0;

        Date currentDate = new Date(currentTime);

        Calendar calCurrentDate = Calendar.getInstance();//使用日历类

        calCurrentDate.setTime(currentDate);


        Date compareDate = new Date(compareTime);

        Calendar calCompareDate = Calendar.getInstance();//使用日历类

        calCompareDate.setTime(compareDate);

        apartNumber = calCompareDate.get(Calendar.DAY_OF_YEAR) - calCurrentDate.get(Calendar.DAY_OF_YEAR);

        return apartNumber;

    }

    /**
     * 补0操作
     *
     * @param i
     * @return
     */
    public static String unitFormat(long i) {
        String retStr = "";
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 获取当前年
     *
     * @return
     */

    public static int getCurrYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }


}
