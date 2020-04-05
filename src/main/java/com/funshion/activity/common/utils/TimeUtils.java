package com.funshion.activity.common.utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 描述:日期操作公共类
 * <p>创建人：zuokeli 创建日期：2015年1月23日 </p>
 *
 * @version V1.0
 */
public class TimeUtils {

    private static final Locale DEFAUTL_LOCALE = Locale.CHINA;


    /**
     * 描述:计算日期累加多少天之后的日期 (输入负数，就是减少天)
     * <p>创建人：zuokeli , 2015年1月24日 上午8:58:42</p>
     *
     * @param inputDate 需要累加的日期
     * @param dayCount  累加的天数
     * @return
     */
    public static Date getDateIncDayCount(Date inputDate, int dayCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.add(Calendar.DAY_OF_YEAR, dayCount);
        return c.getTime();
    }

    /**
     * 加second小时之后日期
     *
     * @param inputDate
     * @param hours
     * @return
     */
    public static Date getDateIncSecondCount(Date inputDate, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.add(Calendar.SECOND, second);
        return c.getTime();
    }


    /**
     * 加hours小时之后日期
     *
     * @param inputDate
     * @param hours
     * @return
     */
    public static Date getDateIncHourCount(Date inputDate, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    /**
     * 加month小时之后日期
     *
     * @param inputDate
     * @param hours
     * @return
     */
    public static Date getDateIncMonthCount(Date inputDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    /**
     * 加years年之后日期
     *
     * @param inputDate
     * @param years
     * @return
     */
    public static Date getDateIncYearCount(Date inputDate, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    /**
     * 描述:计算两个日期之间相差的天数
     * <p>创建人：zuokeli , 2015年1月24日 上午9:00:39</p>
     *
     * @param endDate   结束日期
     * @param startDate 开始日期
     * @return
     */
    public static long calDaysBetweenDate(Date endDate, Date startDate) {
        long days = 0;
        try {
            startDate = formatTime2Date(startDate);
            endDate = formatTime2Date(endDate);
            days = (endDate.getTime() - startDate.getTime()) / 60 / 60 / 1000 / 24;
        } catch (Exception e) {
            throw new RuntimeException("get failed", e);
        }
        return days;
    }

    /**
     * 描述:格式化当前日期（去掉时间信息）
     * <p>创建人：zuokeli , 2015年1月24日 上午9:01:33</p>
     *
     * @param date 需要格式化的日期
     * @return
     */
    public static Date formatTime2Date(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 描述:格式化当前日期(yyyy-MM-dd 23:59:59)
     *
     * @param date
     * @return
     */
    public static Date formatTimeEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 描述:获取日期中的天
     * <p>创建人：zuokeli , 2015年1月24日 上午9:01:48</p>
     *
     * @param date 指定日期
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }


    /**
     * 描述:获取当前日期（时分秒都为0）
     * <p>创建人：zuokeli , 2015年1月24日 上午9:03:28</p>
     *
     * @return
     */
    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 描述:获取当前日期 周
     * <p>创建人：zuokeli , 2015年1月24日 上午9:03:28</p>
     *
     * @return
     */
    public static int getCurrentDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static int getCurrentDayOfYear(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 描述:获取当前时间（包括时分秒）
     * <p>创建人：zuokeli , 2015年1月24日 上午9:03:28</p>
     *
     * @return
     */
    public static Date getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.getTime();
    }

    /**
     * 描述:判断date1和date2是否年和月相,如果相同就返回date2，否则返回date1
     * <p>创建人：zuokeli , 2015年1月24日 上午9:03:51</p>
     *
     * @param date1 需要比较的日期
     * @param date2 需要比较的日期
     * @return
     */
    public static Date isEqualsYM(Date date1, Date date2) {
        Calendar date1Temp = Calendar.getInstance();
        Calendar date2Temp = Calendar.getInstance();
        date1Temp.setTime(date1);
        date2Temp.setTime(date2);
        if ((date1Temp.get(Calendar.YEAR) == date2Temp.get(Calendar.YEAR)) && date1Temp.get(Calendar.MONTH) == date2Temp.get(Calendar.MONTH)) {
            date1 = date2;
        }
        return date1;
    }

    /**
     * 描述:根据指定的还款日对日期进行修约
     * <p>创建人：zuokeli , 2015年1月24日 上午8:37:18</p>
     *
     * @param result 需要修约的日期
     * @param dueDay 指定的还款日
     * @return
     */
    private static Date adjustByDueDay(Date result, int dueDay) {
        Calendar newResult = Calendar.getInstance();
        newResult.setTime(result);
        if (dueDay > 28) {
            int curDueDay = newResult.get(Calendar.DAY_OF_MONTH);
            if (curDueDay != dueDay) {
                int max = newResult.getActualMaximum(Calendar.DAY_OF_MONTH);
                curDueDay = dueDay;
                if (max < dueDay) {
                    curDueDay = max;
                }
                newResult.set(Calendar.DAY_OF_MONTH, curDueDay);
            }
        }
        return newResult.getTime();
    }

    /**
     * 描述:根据起始日期计算下期还款日期
     * <p>创建人：zuokeli , 2015年1月23日 下午9:27:15</p>
     *
     * @param startDate      起始日期
     * @param frequency      还款频率单位
     * @param frequencyRange 还款频率步长
     * @param dueDay         指定还款日
     * @return
     */
    private static Date getDateByPaymentFreq(Date startDate, String frequency, int frequencyRange, int dueDay) {
        if (startDate != null) {
            startDate = formatTime2Date(startDate);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        if (Frequency.PAY_MONTHLY.getCodeInDb().equals(frequency)) {
            cal.add(Calendar.MONTH, frequencyRange);
            return adjustByDueDay(cal.getTime(), dueDay);
        } else if (Frequency.PAY_QUARTERLY.getCodeInDb().equals(frequency)) {
            cal.add(Calendar.MONTH, 3);
            return adjustByDueDay(cal.getTime(), dueDay);
        } else if (Frequency.PAY_YEARLY_HALF.getCodeInDb().equals(frequency)) {
            cal.add(Calendar.MONTH, 6);
            return adjustByDueDay(cal.getTime(), dueDay);
        } else if (Frequency.PAY_WEEKLY.getCodeInDb().equals(frequency)) {
            cal.add(Calendar.DATE, frequencyRange * 7);
            return cal.getTime();
        } else if (Frequency.PAY_WEEKLY_2.getCodeInDb().equals(frequency)) {
            cal.add(Calendar.DATE, frequencyRange * 14);
            return cal.getTime();
        } else if (Frequency.PAY_YEARLY.getCodeInDb().equals(frequency)) {
            cal.add(Calendar.YEAR, frequencyRange);
            return adjustByDueDay(cal.getTime(), dueDay);
        } else {
            throw new RuntimeException("没有找到的日期频率为【" + frequency + "】的还款日！");
        }
    }

    /**
     * 描述:根据起始日期计算出对年对月日期
     * <p>创建人：zuokeli , 2015年1月23日 下午9:06:02</p>
     *
     * @param startDate 起始日期
     * @param months    增加的月数
     * @return
     */
    public static Date getDateByMonths(Date startDate, int months) {
        int dueDay = TimeUtils.getDay(startDate);
        return TimeUtils.getDateByPaymentFreq(startDate, Frequency.PAY_MONTHLY.getCodeInDb(), months, dueDay);
    }

    /**
     * 描述:还款频率公共类
     * <p>创建人：zuokeli 创建日期：2015年1月24日 </p>
     *
     * @version V1.0
     */
    public enum Frequency {
        PAY_WEEKLY, PAY_WEEKLY_2, PAY_MONTHLY, PAY_QUARTERLY, PAY_YEARLY_HALF, PAY_YEARLY, PAY_ALL_ONCE;

        public String getCodeInDb() {
            switch (values()[ordinal()]) {
                case PAY_WEEKLY:
                    return "W";
                case PAY_WEEKLY_2:
                    return "2W";
                case PAY_MONTHLY:
                    return "M";
                case PAY_QUARTERLY:
                    return "Q";
                case PAY_YEARLY_HALF:
                    return "HY";
                case PAY_YEARLY:
                    return "Y";
                case PAY_ALL_ONCE:
                    return "A";
            }
            throw new RuntimeException("not found enum");
        }

        public static Frequency getEnum(String frequency) {
            if ("W".equals(frequency)) {
                return PAY_WEEKLY;
            }
            if ("2W".equals(frequency)) {
                return PAY_WEEKLY_2;
            }
            if ("M".equals(frequency)) {
                return PAY_MONTHLY;
            }
            if ("Q".equals(frequency)) {
                return PAY_QUARTERLY;
            }
            if ("HF".equals(frequency)) {
                return PAY_YEARLY_HALF;
            }
            if ("Y".equals(frequency)) {
                return PAY_YEARLY;
            }
            if ("A".equals(frequency)) {
                return PAY_ALL_ONCE;
            }
            throw new RuntimeException("not found enum");
        }
    }

    /**
     * 根据date转化成string yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAUTL_LOCALE);
        String formatStr = format.format(date);
        return formatStr;
    }

    public static Date parseToDate(String d) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", DEFAUTL_LOCALE);
        return format.parse(d);
    }

    public static Date parseToDateTime(String d) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAUTL_LOCALE);
        return format.parse(d);
    }

    /**
     * 根据date转化成string yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatStringOnly(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", DEFAUTL_LOCALE);
        if (date == null) {
            return null;
        }
        String formatStr = format.format(date);
        return formatStr;
    }

    /**
     * 根据date转化成string yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatMonthDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd", DEFAUTL_LOCALE);
        if (date == null) {
            return null;
        }
        String formatStr = format.format(date);
        return formatStr;
    }

    /**
     * 日期加（x）天，时间归零  *x 为负值时为减
     *
     * @param date
     * @param x    天数
     */
    public static Date addDayAndTimeRZ(Date date, int x) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, x);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 将参数中的时分秒 放置在指定日期年月日
     *
     * @param hmsDate
     * @return
     */
    public static Date parseHMSofDate(Date ymdDate, Date hmsDate) {
        ymdDate = ymdDate == null ? new Date() : ymdDate;
        hmsDate = hmsDate == null ? new Date() : hmsDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(hmsDate);
        Integer hour = cal.get(Calendar.HOUR_OF_DAY), minute = cal.get(Calendar.MINUTE), second = cal.get(Calendar.SECOND);
        cal.setTime(ymdDate);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return cal.getTime();
    }

    /**
     * 将XMLGregorianCalendar转化为Date类型
     *
     * @param cal
     * @return
     */
    public static Date xmlDate2Date(XMLGregorianCalendar cal) {
        return cal.toGregorianCalendar().getTime();
    }

    /**
     * 计算current日期是否在start和end之间，包括相等。
     *
     * @param current 目标日期
     * @param start   开始时间
     * @param end     结束时间
     * @return
     */
    public static boolean between(Date current, Date start, Date end) {
        if (current == null || start == null || end == null)
            return false;
        if (end.before(start)) {
            throw new IllegalArgumentException("start can not big than end");
        }
        int s = current.compareTo(start);//0,1
        int e = current.compareTo(end);//-1,0
        int c = s + e;
        return c != -2 && c != 2;
    }

    /**
     * 计算精确到秒的时间戳。
     *
     * @param date
     * @return 单位为秒的时间戳
     */
    public static int getIntTime(Date date) {
        return (int) (date.getTime() / 1000);
    }

    /**
     * 判断时间先后。
     *
     * @param date1
     * @param date2
     * @return date1是否早于date2
     */
    public static boolean before(Date date1, Date date2) {
        return date1.before(date2);
    }


    public static int getSeconds(Date date) {
        return (int) ((date.getTime()) / 1000);
    }

    public static Date getLastDayOfMonth() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }


    public static Date getLastDayOfMonth(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTime();
    }

    public static Date getLastSecondOfDay() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }

    public static Date getLastSecondOfWeek() {
        Calendar ca = Calendar.getInstance();
        ca.setFirstDayOfWeek(Calendar.MONDAY);
        ca.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }

}