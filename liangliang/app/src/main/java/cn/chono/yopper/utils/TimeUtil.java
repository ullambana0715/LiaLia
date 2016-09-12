package cn.chono.yopper.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }


    public static String getTime(long time,String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(new Date(time));
    }


    /**
     * 判断两时间是否相差超过60秒 超过返回true
     */
    public static boolean getIntervalDays(long curr_timesamp, long pre_timesamp) {

        Date curr_day = new Date(curr_timesamp);
        Date pre_day = new Date(pre_timesamp);

        if (curr_day.after(pre_day)) {
            Date swap = curr_day;
            curr_day = pre_day;
            pre_day = swap;
        }
        long start = curr_day.getTime();
        long end = pre_day.getTime();
        long interval = end - start;
        long ms = (long) (interval / 1000);
        if (ms >= 60) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 返回两日期之间相差的天数
     * <p>
     * 两时间相差的分钟数
     */
    public static int getdifMin(long curr_timesamp, long pre_timesamp) {

        Date curr_day = new Date(curr_timesamp);
        Date pre_day = new Date(pre_timesamp);

        if (curr_day.after(pre_day)) {
            Date swap = curr_day;
            curr_day = pre_day;
            pre_day = swap;
        }
        long start = curr_day.getTime();
        long end = pre_day.getTime();
        long interval = end - start;
        int min = (int) ((interval / 1000) / 60);
        return min;

    }

    /**
     * 返回两日期之间相差的天数
     *
     * @param startday
     * @param endday
     * @return
     */
    public static int getIntervalDays(Date startday, Date endday) {
        if (startday.after(endday)) {
            Date swap = startday;
            startday = endday;
            endday = swap;
        }
        long start = startday.getTime();
        long end = endday.getTime();
        long interval = end - start;
        return (int) (interval / (1000 * 60 * 60 * 24));
    }

    /**
     * 判断毫秒数是不是在今天，只支持GMT+8时区
     *
     * @param value
     * @return
     */
    public static boolean isToday(long value) {

        long system_time = System.currentTimeMillis();

        Date startday = new Date(value);
        Date endday = new Date(system_time);

        return isToday(startday, endday);
    }


    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(final Date date, final Date nowdate) {
        return isTheDay(date, nowdate);
    }

    /**
     * 是否是指定日期
     *
     * @param date
     * @param day
     * @return
     */
    public static boolean isTheDay(final Date date, final Date day) {
        return date.getTime() >= dayBegin(day).getTime()
                && date.getTime() <= dayEnd(day).getTime();
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date dayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }


    /**
     * 返回日期的字符串表示形式：yyyy年MM月dd日 HH:mm
     */
    // 实现日期固定格式表示
    @SuppressWarnings("deprecation")
    public static String getDateTimeString(long timesamp) {
        Date date = new Date(timesamp);

        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();

        String timeString = year + "年" + (month < 10 ? "0" + month : month)
                + "月" + (day < 10 ? "0" + day : day) + "日  "
                + (hour < 10 ? "0" + hour : hour) + ":"
                + (minute < 10 ? "0" + minute : minute);

        return timeString;
    }

    /**
     * 是否是今年 如果不是今年 返回yy-MM-dd 是今年返回日期的字符串表示形式：hh-mm
     */
    public static String getDateString(long timesamp, long systime) {
        Date date = new Date(timesamp);
        Date system_date = new Date(systime);
        int sys_year = system_date.getYear() + 1900;
        int year = date.getYear() + 1900;

        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();
        String timeString = "";
        if (sys_year != year) {

            timeString = year + "-" + (month < 10 ? "0" + month : month) + "-"
                    + (day < 10 ? "0" + day : day);
        } else {

            timeString = (month < 10 ? "0" + month : month) + "-"
                    + (day < 10 ? "0" + day : day);
        }

        return timeString;
    }


    public static String getDatingDateString(long timesamp, long systime) {
        Date date = new Date(timesamp);
        Date system_date = new Date(systime);
        int sys_year = system_date.getYear() + 1900;
        system_date.getMonth();
        int year = date.getYear() + 1900;

        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();
        String timeString = "";

        if (sys_year != year) {
            timeString = year + "年" + (month < 10 ? "0" + month : month) + "月" + (day < 10 ? "0" + day : day) + "日";
        } else {
            int dateTime = getIntervalDays(date, system_date);
            if (dateTime == 0) {
                timeString = (month < 10 ? "0" + month : month) + "月" + (day < 10 ? "0" + day : day) + "日(今天)";
            } else {
                timeString = (month < 10 ? "0" + month : month) + "月" + (day < 10 ? "0" + day : day) + "日";
            }
        }

        return timeString;
    }


    public static String getDatingPublishDateString(long timesamp, long systime) {

        String timeString = "";

        if (timesamp >= systime) {
            timeString = "刚刚";
            return timeString;
        }

        Date date = new Date(timesamp);

        Date system_date = new Date(systime);
        int sys_year = system_date.getYear() + 1900;
        int sys_month = system_date.getMonth() + 1;
        int sys_day = system_date.getDate();
        int system_hour = system_date.getHours();
        int system_minute = system_date.getMinutes();

        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();


        if (sys_year != year) {

            timeString = year + "年" + (month < 10 ? "0" + month : month) + "月" + (day < 10 ? "0" + day : day) + "日";

        } else {

            int dateTime = Math.abs(getIntervalDays(date, system_date));

            if (dateTime == 0) {

                int hour_diff = 0;

                if (day == sys_day) {
                    hour_diff = Math.abs(system_hour - hour);
                } else {
                    hour_diff = Math.abs(system_hour + 24 - hour);
                }

                if (hour_diff == 0) {

                    int min_diff = Math.abs(system_minute - minute);

                    if (min_diff == 0) {

                        timeString = "刚刚";

                    } else {

                        timeString = min_diff + "分钟前";

                    }

                } else {
                    timeString = hour_diff + "小时前";
                }

            } else if (dateTime >= 1 && dateTime < 7) {

                timeString = dateTime + "天前";

            } else {

                timeString = "一周前";

            }
        }

        return timeString;
    }


    /**
     * 返回yy-MM-dd
     */
    public static String getDateFormatString(long timesamp) {

        Date date = new Date(timesamp);
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();

        String timeString = "";

        timeString = year + "-" + (month < 10 ? "0" + month : month) + "-"
                + (day < 10 ? "0" + day : day);


        return timeString;
    }

    /**
     * 返回yy-MM-dd
     */
    public static String getDateFormatString(long timesamp, long sytime) {

        Date date = new Date(timesamp);
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();

        Date systemdate = new Date(sytime);
        int curyear = systemdate.getYear() + 1900;

        String timeString = "";

        if (curyear == year) {

            timeString = (month < 10 ? "0" + month : month) + "-"
                    + (day < 10 ? "0" + day : day);
        } else {

            timeString = year + "-" + (month < 10 ? "0" + month : month) + "-"
                    + (day < 10 ? "0" + day : day);
        }


        return timeString;
    }

    /**
     * 返回日期的字符串表示形式：HH:mm
     */
    // 实现日期固定格式表示
    @SuppressWarnings("deprecation")
    public static String gethour_minString(long timesamp) {
        Date date = new Date(timesamp);

        int hour = date.getHours();
        int minute = date.getMinutes();

        String timeString = (hour < 10 ? "0" + hour : hour) + ":"
                + (minute < 10 ? "0" + minute : minute);

        return timeString;
    }


    /**
     * 获取百纳秒
     *
     * @param @return 设定文件
     * @return long 返回类型
     * @throws
     * @Title: getHundredNanosecond
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static long getHundredNanosecond() {
        return System.currentTimeMillis() * 1000 * 10;

    }

    /**
     * 获取当前系统时间，单位--秒
     *
     * @param @return 设定文件
     * @return long 返回类型
     * @throws
     * @Title: getCurrentNanosecondTime
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }


    /**
     * 常规时间转换
     *
     * @param datetime
     * @return
     */

    public static String normalTimeFormat(long datetime) {

        String time = "";

        long system_time = System.currentTimeMillis();


        Date startday = new Date(datetime);
        Date endday = new Date(system_time);
        int dateTime = getIntervalDays(startday, endday);

        if (dateTime == 0) {
            // 是当天

            int min = TimeUtil.getdifMin(datetime, system_time);

            if (min == 0) {
                time = "刚刚";
            } else if (1 <= min && min < 60) {
                time = min + "分钟前";
            } else {
                int h_main = 0;

                h_main = Integer.valueOf((min / 60));// 转小时

                time = h_main + "小时前";
            }

        } else {
            // 消息时间不是当天
            time = TimeUtil.getDateString(datetime, system_time);
        }

        return time;
    }


    /**
     * 活沃时间转换
     *
     * @param datetime
     * @return
     */

    public static String LivelyTimeFormat(long datetime) {

        String time = "";

        long system_time = System.currentTimeMillis();

        Date startday = new Date(datetime);

        Date endday = new Date(system_time);

        int dateTime = getIntervalDays(startday, endday);

        if (dateTime == 0) {
            // 是当天

            int min = TimeUtil.getdifMin(datetime, system_time);

            if (min == 0) {

                time = "刚刚";

            } else if (1 <= min && min < 60) {

                time = min + "分钟前";

            } else {

                int h_main = 0;

                h_main = Integer.valueOf((min / 60));// 转小时

                time = h_main + "小时前";
            }

        } else {

            if (dateTime >= 7) {

                time ="一周前";

            } else {

                time = dateTime + "天前";
            }


        }

        return time;
    }


    /**
     * 取得某个月有多少天
     */

    public static int getDaysOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int days_of_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days_of_month;
    }

    /**
     * 取得当前年
     */

    public static int getDays() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);

    }

    /**
     * 取得当前月
     */

    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;////由于月份是从0开始的所以加1
    }

    /**
     * 取得当前日
     */

    public static int getDate() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public static String getWeek(int year, int month, int day) {

        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        Calendar c = Calendar.getInstance();
        c.set(year, month - 1,
                day);

        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];

    }


}
