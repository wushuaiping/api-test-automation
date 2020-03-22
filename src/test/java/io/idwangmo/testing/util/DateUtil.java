package io.idwangmo.testing.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ts
 * 2019-01-22
 * 14:23
 */
public class DateUtil {
    // 完整时间
    public static final String simple = "yyyy-MM-dd HH:mm:ss";

    // private static final DateFormat simple = new SimpleDateFormat();
    // 年月日
    public static final String dtSimple = "yyyy-MM-dd";
    // 年月日
    public static final String dayPointCutSimple = "yyyy.MM.dd";

    // private static final DateFormat dtSimple = new
    // SimpleDateFormat("yyyy-MM-dd");
    // 年月日
    public static final String dtSimpleChinese = "yyyy年MM月dd日";

    public static final String dtYM = "yyyyMM";

    // private static final DateFormat dtSimpleChinese = new
    // SimpleDateFormat("yyyy年MM月dd日");
    // 年月日(无下划线)
    public static final String dtShort = "yyyyMMdd";

    // 年月日时分秒(无下划线)
    public static final String dtLong = "yyyyMMddHHmmss";

    // 年月日时分秒(年份只取两位)
    public static final String yyssFormat = "yyMMddHHmmss";

    // private static final DateFormat dtShort = new
    // SimpleDateFormat("yyyyMMdd");
    // 时分秒
    public static final String hmsFormat = "HH:mm:ss";

    // private static final DateFormat hmsFormat = new
    // SimpleDateFormat("HH:mm:ss");
    public static final String simpleMinute = "yyyy-MM-dd HH:mm";

    // 年月日时分秒毫秒(无下划线)
    public static final String dtLongMill = "yyyyMMddHHmmssS";

    // private static final DateFormat simpleFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static DateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }


    /**
     * yyMMddHHmmss
     *
     * @param date 时间
     * @return 时间
     */
    public static final String yyssFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(yyssFormat).format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间
     * @return 时间
     */
    public static final String simpleFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(simple).format(date);
    }

    /**
     * yyyy-MM-dd
     *
     * @param date 时间
     * @return 时间
     */
    public static final String dtSimpleFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtSimple).format(date);
    }

    /**
     * yyyyMMddHHmmssS
     *
     * @param date 时间
     * @return 时间
     */
    public static final String stringDtLongMill(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtLongMill).format(date);
    }

    /**
     * yyyy-mm-dd 日期格式转换为日期
     *
     * @param strDate 时间字符串
     * @return 时间
     */
    public static final Date dateFromDayDate(String strDate) {
        if (strDate == null) {
            return null;
        }

        try {
            return getFormat(dtSimple).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * yyyy-mm-dd 日期格式转换为日期
     *
     * @param strDate 时间字符串
     * @return 时间
     */
    public static final Date dateFromSimple(String strDate) {
        if (strDate == null) {
            return null;
        }

        try {
            return getFormat(simple).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date getDtSimpleDay(Date date) {
        return dateFromStringFormat(stringDayUserDefined(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    /**
     * yyyy-MM-dd HH:mm 日期格式转换为日期
     *
     * @param strDate 时间字符串
     * @return 时间
     */
    public static final Date dateFromSimpleMinute(String strDate) {
        if (strDate == null) {
            return null;
        }

        try {
            return getFormat(simpleMinute).parse(strDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * yyyy-MM-dd HH:mm 或者yyyy-MM-dd  转换为日期格式
     *
     * @param strDate 时间字符串
     * @return 时间
     */
    public static final Date dateFromStrDayOrMinute(String strDate) {
        if (dateFromSimpleMinute(strDate) != null) {
            return dateFromSimpleMinute(strDate);
        } else {
            return dateFromDayDate(strDate);
        }

    }

    /**
     * 获取输入日期的相差日期
     *
     * @param dt    参数
     * @param idiff 参数
     * @return 时间
     */
    public static final String getDayFromDiffDate(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();

        c.setTime(dt);
        c.add(Calendar.DATE, idiff);
        return dtSimpleFormat(c.getTime());
    }

    /**
     * 获取输入日期月份的相差日期
     *
     * @param dt    参数
     * @param idiff 参数
     * @return 时间
     */
    public static final String getDayDiffMon(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, idiff);
        return dtSimpleFormat(c.getTime());
    }

    /**
     * yyyy年MM月dd日
     *
     * @param date 时间
     * @return 时间
     */
    public static final String stringDtSimpleChineseFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtSimpleChinese).format(date);
    }

    /**
     * yyyy-MM-dd到 yyyy年MM月dd日 转换
     *
     * @param date 时间
     * @return 时间
     * @throws ParseException canshu
     */
    public static final String formatChangeDtSimpleChinese(String date) throws ParseException {
        if (date == null) {
            return "";
        }

        return getFormat(dtSimpleChinese).format(dateFromstringDay(date));
    }

    /**
     * yyyy-MM-dd 日期字符转换为时间
     *
     * @param stringDate 参数
     * @return aaa
     * @throws ParseException aaa
     */
    public static final Date dateFromstringDay(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }

        return getFormat(dtSimple).parse(stringDate);
    }

    /**
     * 计算日期差值
     *
     * @param afterDate aaa
     * @param beforDate aaa
     * @return 时间 int（天数）
     */
    public static final int countDays(Date beforDate, Date afterDate) {
        return (int) (countMilliseconds(beforDate, afterDate) / 1000 / 3600 / 24);
    }

    /**
     * 得到两日期相差几个月
     *
     * @param startDate aaa
     * @param endDate   aaa
     * @return 时间
     */
    public static int countMonth(Date startDate, Date endDate) {
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);

        int sYear = starCal.get(Calendar.YEAR);
        int sMonth = starCal.get(Calendar.MONTH);
        int sday = starCal.get(Calendar.DAY_OF_MONTH);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int eYear = endCal.get(Calendar.YEAR);
        int eMonth = endCal.get(Calendar.MONTH);
        int eDay = endCal.get(Calendar.DAY_OF_MONTH);
        //如果超过一天也算一个月
        int monthRealDiff = eMonth - sMonth + ((eDay - sday) >= 0 ? 1 : 0);

        return ((eYear - sYear) * 12 + monthRealDiff);
    }

    public static int countYear(Date startDate, Date endDate) {
        int countMonth = countMonth(startDate, endDate);
        int y1 = countMonth / 12;
        int y2 = countMonth % 12;
        return y2 == 0 ? y1 : y1 + 1;
    }

    /**
     * 计算两个日期的相差几个自然月，如5月-10月，6个自然月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int countCalendarMonth(Date startDate, Date endDate) {
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);

        int sYear = starCal.get(Calendar.YEAR);
        int sMonth = starCal.get(Calendar.MONTH);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int eYear = endCal.get(Calendar.YEAR);
        int eMonth = endCal.get(Calendar.MONTH);
        //如果超过一天也算一个月
        int monthRealDiff = eMonth - sMonth + 1;

        return ((eYear - sYear) * 12 + monthRealDiff);
    }

    /**
     * 计算时间差
     *
     * @param endTime   aaa
     * @param startTime aaa
     * @return 时间 时间差(毫秒)
     */
    public static final long countMilliseconds(Date startTime, Date endTime) {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 返回日期时间（Add by Gonglei）
     *
     * @param stringDate (yyyyMMdd)
     * @return 时间
     * @throws ParseException aaa
     */
    public static final Date dateDtDate(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        return getFormat(dtShort).parse(stringDate);
    }


    /**
     * 返回短日期格式（yyyyMMdd格式）
     *
     * @param Date date
     * @return 时间
     */
    public static final String shortDate(Date Date) {
        if (Date == null) {
            return null;
        }

        return getFormat(dtShort).format(Date);
    }

    /**
     * 返回长日期格式（yyyyMMddHHmmss格式）
     *
     * @param Date date
     * @return 时间
     */
    public static final String longDate(Date Date) {
        if (Date == null) {
            return null;
        }

        return getFormat(dtLong).format(Date);
    }

    /**
     * yyyy-MM-dd 日期字符转换为长整形
     *
     * @param stringDate 参数
     * @return 时间
     * @throws ParseException aaa
     */
    public static final Long getLongFromStringDayDate(String stringDate) throws ParseException {
        Date d = dateFromstringDay(stringDate);
        if (d == null) {
            return null;
        }
        return new Long(d.getTime());
    }

    /**
     * 日期转换为字符串 HH:mm:ss
     *
     * @param date 时间
     * @return 时间
     */
    public static final String hmsFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(hmsFormat).format(date);
    }

    /**
     * 时间转换字符串 2005-06-30 15:50
     *
     * @param date 时间
     * @return 时间
     */
    public static final String simpleDate(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(simpleMinute).format(date);
    }

    /**
     * 获取当前日期的日期差 now= 2005-07-19 diff = 1 -&gt; 2005-07-20 diff = -1 -&gt;. 2005-07-18
     *
     * @param diff 参数
     * @return 时间
     */
    public static final String getDayDiffDate(int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        return dtSimpleFormat(c.getTime());
    }

    public static final Date dateFromDiffDateTime(int diff) {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        return c.getTime();
    }

    /**
     * 获取当前日期的日期时间差
     *
     * @param diff  相差天数
     * @param hours 相差小时数
     * @return 时间
     */
    public static final String getTimeFromDiffDateHours(int diff, int hours) {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        c.add(Calendar.HOUR, hours);
        return simpleFormat(c.getTime());
    }

    /**
     * 获取下日期格式相同的相差天数
     *
     * @param srcDate 参数
     * @param format  参数
     * @param diff    参数
     * @return 时间
     */
    public static final String getFormatFromDiffDay(String srcDate, String format, int diff) {
        DateFormat f = new SimpleDateFormat(format);

        try {
            Date source = f.parse(srcDate);
            Calendar c = Calendar.getInstance();

            c.setTime(source);
            c.add(Calendar.DATE, diff);
            return f.format(c.getTime());
        } catch (Exception e) {
            return srcDate;
        }
    }

    /**
     * 把日期类型的日期换成数字类型 YYYYMMDD类型
     *
     * @param date 时间
     * @return 时间
     */
    public static final Long convNumberFromDate(Date date) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();

        c.setTime(date);

        String month;
        String day;

        if ((c.get(Calendar.MONTH) + 1) >= 10) {
            month = "" + (c.get(Calendar.MONTH) + 1);
        } else {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        if (c.get(Calendar.DATE) >= 10) {
            day = "" + c.get(Calendar.DATE);
        } else {
            day = "0" + c.get(Calendar.DATE);
        }

        String number = c.get(Calendar.YEAR) + "" + month + day;

        return new Long(number);
    }

    /**
     * 获取下月
     *
     * @param StringDate dddd
     * @return 时间
     * @throws ParseException aaa
     */
    public static String getNextMon(String StringDate) throws ParseException {
        Date tempDate = DateUtil.dateDtDate(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.MONTH, 1);
        return DateUtil.shortDate(cad.getTime());
    }

    /**
     * 获取下月
     *
     * @param StringDate dddd
     * @return 时间
     * @throws ParseException aaa
     */
    public static String getNextMonUserDefined(String StringDate, String format) throws ParseException {
        Date tempDate = DateUtil.dateFromStringFormat(StringDate, format);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.MONTH, 1);
        return DateUtil.stringDayUserDefined(cad.getTime(), format);
    }

    /**
     * add by daizhixia 20050808 获取下一天
     *
     * @param StringDate 参数
     * @return 时间
     * @throws ParseException aaa
     */
    public static String getNextDay(String StringDate) throws ParseException {
        Date tempDate = DateUtil.dateFromstringDay(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, 1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by chencg 获取下一天 返回 dtSimple 格式字符
     *
     * @param date 时间
     * @return 时间
     * @throws ParseException aaa
     */
    public static String getNextDay(Date date) throws ParseException {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(Calendar.DATE, 1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * @param StringDate 参数
     * @return 时间
     * @throws ParseException aaa
     */
    public static String getDayBeforeDay(String StringDate) throws ParseException {
        Date tempDate = DateUtil.dateFromstringDay(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, -1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * @param date 时间
     * @return 时间
     * @throws ParseException aaa
     */
    public static String getDayBeforeDay(Date date) throws ParseException {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(Calendar.DATE, -1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by chencg 获取下一天 返回 dtshort 格式字符
     *
     * @param StringDate "20061106"
     * @return 时间 String "2006-11-07"
     * @throws ParseException aaaa
     */
    public static Date getNextDayDtShort(String StringDate) throws ParseException {
        Date tempDate = DateUtil.dateDtDate(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, 1);
        return cad.getTime();
    }

    /**
     * add by daizhixia 20050808 取得相差的天数
     *
     * @param startDate aaa
     * @param endDate   aaa
     * @return 时间
     */
    public static int countDays(String startDate, String endDate) {
        int days = 0;

        try {
            Date tempDate1 = DateUtil.dateFromstringDay(startDate);

            Date tempDate2 = DateUtil.dateFromstringDay(endDate);
            days = (int) ((tempDate2.getTime() - tempDate1.getTime()) / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    public static Date now() {
        return new Date();
    }

    /**
     * 获取传入时间相差的日期
     *
     * @param dt   传入日期，可以为空
     * @param diff 需要获取相隔diff天的日期 如果为正则取以后的日期，否则时间往前推
     * @return 时间
     */
    public static String getTimeDiffDate(Date dt, int diff) {
        Calendar ca = Calendar.getInstance();

        if (dt == null) {
            ca.setTime(new Date());
        } else {
            ca.setTime(dt);
        }

        ca.add(Calendar.DATE, diff);
        return simpleFormat(ca.getTime());
    }

    /**
     * 将字符串按format格式转换为date类型
     *
     * @param str    参数
     * @param format 参数
     * @return 时间
     */
    public static Date dateFromStringFormat(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 加减天数
     *
     * @param aDate aaa
     * @param days  aaa
     * @return 时间 Date
     * @author shencb 2006-12 add
     */
    public static final Date addDays(Date aDate, int days) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(aDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 加减小时数
     *
     * @param aDate
     * @param hours
     * @return
     */
    public static final Date addHours(Date aDate, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aDate);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    /**
     * 加减分钟数
     *
     * @param aDate
     * @param minutes
     * @return
     */
    public static final Date addMinutes(Date aDate, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aDate);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * 把日期2007/06/14转换为20070614
     *
     * @param date 时间
     * @return 时间
     * @author Yufeng 2007
     */
    public static String formatChangeDayDateString(String date) {
        String result = "";
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        if (date.length() == 10) {
            result = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
        }
        return result;
    }

    /**
     * 获得日期是周几
     *
     * @param date 时间
     * @return 时间 dayOfWeek
     * @author xiang.zhaox
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得日期是周几
     *
     * @param date 时间
     * @return 时间 dayOfWeek
     * @author xiang.zhaox
     */
    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 本年第一天
     *
     * @param date 时间
     * @return 时间 dayOfWeek
     * @author xiang.zhaox
     */
    public static Date getFirstDateOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFirstDateOfMonth(date));
        cal.set(Calendar.MONTH, 0);
        return cal.getTime();
    }

    /**
     * 本月第一天
     *
     * @param date 时间
     * @return 时间 dayOfWeek
     * @author xiang.zhaox
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getLastDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }


    /**
     * 获取下月
     *
     * @param month   参数
     * @param srcDate 参数
     * @return 时间
     */
    public static Date addMonth(Date srcDate, Integer month) {
        Calendar cad = Calendar.getInstance();

        cad.setTime(srcDate);
        cad.add(Calendar.MONTH, month);
        return cad.getTime();
    }

    /**
     * 获取当前月的天数
     *
     * @param date
     * @return
     */
    public static int getTotalDayOfMonth(Date date) {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        return cad.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String stringDayPointCut(Date date) {
        return getFormat(dayPointCutSimple).format(date);
    }

    public static String stringDayUserDefined(Date date, String format) {
        return getFormat(format).format(date);
    }

    public static Date getDateOfTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartOfHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, hour, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(year, month, day, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(year, month, day, hour, minute, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static boolean isDateOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        //相同时也是不重叠
        if (startDate1.equals(endDate2) || startDate2.equals(endDate1)) {
            return false;
        }
        //找出两种不重叠的情况，不符合这些的就是重叠
        return !(startDate1.after(endDate2) || startDate2.after(endDate1));
    }
}
