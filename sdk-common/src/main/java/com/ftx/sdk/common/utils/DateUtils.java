package com.ftx.sdk.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：日期工具
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-06 13:34
 */
public class DateUtils {

    public static String defaultSimpleFormater = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT1 = "yyyy/MM/dd HH:mm:ss";

    /**
     * 默认简单日期字符串
     *
     * @return
     */
    public static String getDefaultSimpleFormater() {
        return defaultSimpleFormater;
    }

    /**
     * 设置默认简单日期格式字符串
     *
     * @param defaultFormatString
     */
    public static void setDefaultSimpleFormater(String defaultFormatString) {
        DateUtils.defaultSimpleFormater = defaultFormatString;
    }

    /**
     * 两个日期是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 两个日期是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(long date1, long date2) {
        return isSameDate(new Date(date1), new Date(date2));
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param formatString
     * @return
     */
    public static String format(Date date, String formatString) {
        if(date == null){
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(formatString);
        return df.format(date);
    }

    /**
     * 格式化日期(使用默认格式)
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, defaultSimpleFormater);
    }

    /**
     * 转换成日期
     *
     * @param dateString
     * @param formatString
     * @return
     */
    public static Date parse(String dateString, String formatString) {
        SimpleDateFormat df = new SimpleDateFormat(formatString);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 转换成日期(使用默认格式)
     *
     * @param dateString
     * @return
     */
    public static Date parse(String dateString) {
        return parse(dateString, defaultSimpleFormater);
    }

    /**
     * 昨天
     *
     * @return
     */
    public static Date yesterday() {
        return addDay(-1);
    }

    /**
     * 明天
     *
     * @return
     */
    public static Date tomorrow() {
        return addDay(1);
    }

    /**
     * 现在
     *
     * @return
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 按日加
     *
     * @param value
     * @return
     */
    public static Date addDay(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, value);
        return now.getTime();
    }

    /**
     * 按日加,指定日期
     *
     * @param date
     * @param value
     * @return
     */
    public static Date addDay(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DAY_OF_YEAR, value);
        return now.getTime();
    }

    /**
     * 按月加
     *
     * @param value
     * @return
     */
    public static Date addMonth(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, value);
        return now.getTime();
    }

    /**
     * 按月加,指定日期
     *
     * @param date
     * @param value
     * @return
     */
    public static Date addMonth(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MONTH, value);
        return now.getTime();
    }

    /**
     * 按年加
     *
     * @param value
     * @return
     */
    public static Date addYear(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, value);
        return now.getTime();
    }

    /**
     * 按年加,指定日期
     *
     * @param date
     * @param value
     * @return
     */
    public static Date addYear(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.YEAR, value);
        return now.getTime();
    }

    /**
     * 按小时加
     *
     * @param value
     * @return
     */
    public static Date addHour(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, value);
        return now.getTime();
    }

    /**
     * 按小时加,指定日期
     *
     * @param date
     * @param value
     * @return
     */
    public static Date addHour(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.HOUR_OF_DAY, value);
        return now.getTime();
    }

    /**
     * 按分钟加
     *
     * @param value
     * @return
     */
    public static Date addMinute(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, value);
        return now.getTime();
    }

    /**
     * 按分钟加,指定日期
     *
     * @param date
     * @param value
     * @return
     */
    public static Date addMinute(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MINUTE, value);
        return now.getTime();
    }

    /**
     * 年份
     *
     * @return
     */
    public static int year() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    /**
     * 月份
     *
     * @return
     */
    public static int month() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH);
    }

    /**
     * 日(号)
     *
     * @return
     */
    public static int day() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 小时(点)
     *
     * @return
     */
    public static int hour() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR);
    }

    /**
     * 分钟
     *
     * @return
     */
    public static int minute() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MINUTE);
    }

    /**
     * 秒
     *
     * @return
     */
    public static int second() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.SECOND);
    }

    /**
     * 星期几(礼拜几)
     *
     * @return
     */
    public static int weekday() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 是上午吗?
     *
     * @return
     */
    public static boolean isAm() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.AM_PM) == 0;
    }

    /**
     * 是下午吗?
     *
     * @return
     */
    public static boolean isPm() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.AM_PM) == 1;
    }

    /**
     *
     * @return 当前的时间戳 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowUnix() {

        return DateTimeToUnix(new Date());

    }

    /**
     * 获取时间戳
     *
     * @param date
     *            需要转化时间戳的时间
     * @return 时间戳
     */
    public static String DateTimeToUnix(Date date) {

        return Long.toString(date.getTime());

    }

    /**
     * 获取格式化时间字符串
     *
     * @return 格式化时间字符串
     */
    public static String formatNow() {

        return DateUtils.format(DateUtils.now());

    }

    public static Timestamp getCurrentTimestamp() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime;
    }

    /**
     * 将日期转换成当前月份1号的日期
     *
     * @return 当前月的1号所对应的日期
     */
    public static Date coverToHourDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 将日期转换成当前月份1号的日期
     *
     * @return 当前月的1号所对应的日期
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new Date(cal.getTimeInMillis());
    }

    public static Date getLastDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 创建时间： 2013-8-13 下午12:32:09
     * 创建人：wuhaifei
     * 参数：
     * 返回值： long
     * 方法描述 : 获取指定日期的开始时间
     */
    public static long getOneDayFirstMoment(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(date);
        now = now + " 00:00:00";

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf2.parse(now).getTime();
        } catch (ParseException e) {
        }
        return 0L;
    }
    /**
     * 日期的添加
     * @param date1 被添加的日期
     * @param dayOrOther  添加的模式:Calendar.DAY_OF_MONTH
     * @param num 添加的数量
     * @return
     */
    public static Date dateAdd(Date date1,int dayOrOther,int num){
        Calendar ca = Calendar.getInstance();
        ca.setTime(date1);
        System.out.println(ca.get(dayOrOther));// 今天的日期
        ca.set(dayOrOther, ca.get(dayOrOther) + num);// 让日期加1
        System.out.println(ca.get(Calendar.DATE));// 加1之后的日期Top
        date1.setTime(ca.getTimeInMillis());

        return date1;
    }
    /**
     * 两个日期的天数差
     * @param bigDay
     * @param smallDay
     * @return
     * @throws ParseException
     */
    public static int subByDay(Date bigDay, Date smallDay) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        bigDay = sdf.parse(sdf.format(bigDay));
        smallDay = sdf.parse(sdf.format(smallDay ));
        Calendar cal = Calendar.getInstance();
        cal.setTime(bigDay);
        long time1 = cal.getTimeInMillis();
        cal.setTime(smallDay );
        long time2 = cal.getTimeInMillis();
        long between_days=(time1-time2)/(1000*3600*24);

        return (int) between_days;
    }
    /**
     *  @deprecated  计算年龄
     * @param longTime
     * @return age
     */
    public static int CalculationAge(long longTime) {
        Date birTime = new Date(longTime);
        return getAgeByBirthday(birTime);
    }

    public static int getAgeByBirthday(Date birthday) {
        Date nowTime = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(nowTime);
        int nowYear = ca.get(Calendar.YEAR);
        int nowMonth = ca.get(Calendar.MONTH);
        int nowDay = ca.get(Calendar.DAY_OF_MONTH);

        ca.setTime(birthday);
        int birYear = ca.get(Calendar.YEAR);
        int birMonth = ca.get(Calendar.MONTH);
        int birDay = ca.get(Calendar.DAY_OF_MONTH);

        int age = nowYear - birYear;

        //判断月份
        if(birMonth>nowMonth){
            age--;
        }else if(birMonth==nowMonth){//月份相同情况下
            //判断天数
            if(birDay>nowDay){//天数没达到
                age--;
            }
            //天数等于或大与，那么就是达到岁数了
        }
        if(age<0){
            return -1;
        }
        return age;
    }

    /**
     * int compare 返回一个基本类型的整型，
     * 返回负数表示：o1 小于o2，
     * 返回0 表示：o1和o2相等，
     * 返回正数表示：o1大于o2
     * @author goff.yin
     * @date 2017-2-21 下午3:58:40
     * @version 1.0.0
     * @param time1
     * @param time2
     * @return
     */
    public static int compareTime(Date time1, Date time2) {
        if (time1.getTime() > time2.getTime()) {
            return 1;
        } else if (time1.getTime() < time2.getTime()) {
            return -1;
        } else {//相等
            return 0;
        }
    }

    /**
     * 获取周数
     * @author goff.yin
     * @date 2017-3-31 下午5:55:34
     * @version 1.0.0
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        if(date == null){
            date = new Date();
        }
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        //now.get(Calendar.WEEK_OF_YEAR) ;
        return now.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 当前天数是这个月第几天
     * @author goff.yin
     * @date 2017-3-31 下午7:13:53
     * @version 1.0.0
     * @return
     */
    public static int dayOfMonth() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getDayStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取一天的开始时间
     * @author goff.yin
     * @date 2017-10-23 下午4:06:26
     * @version 1.0.0
     * @param date
     * @return
     */
    public static Date getDayEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 给时间添加秒
     * @author goff.yin
     * @date 2017-10-26 上午11:26:50
     * @version 1.0.0
     * @param effectDate
     * @param expires
     * @return
     */
    public static Date addSecond(Date effectDate, int expires) {
        Calendar now = Calendar.getInstance();
        now.setTime(effectDate);
        now.add(Calendar.SECOND, expires);
        return now.getTime();
    }


    public static void main(String[] args) {
        Date da1 = new Date();
        System.out.println("da1:"+DateUtils.format(da1));
        da1 = addSecond(da1, 120);
        System.out.println("da1:"+DateUtils.format(da1));

        Date da3 = new Date();
        System.out.println(DateUtils.format(DateUtils.addDay(da3, -27)));

    }

    /**
     *  @Description:比较两个时间相差多少毫秒
     * @Author: xiaojun.yin
     * @param date
     * @param createTime
     * @return
     */
    public static Long diffValueTwoTime(Date date, Date createTime) {
        long time1 = date.getTime();
        long time2 = createTime.getTime();
        return Math.abs(time2 - time1);
    }
}

