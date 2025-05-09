package com.wx.genealogy.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author hangyi
 * @Description 日期时间
 * @Date 15:08 2020/6/11
 * @Param
 * @return
 **/
public class DateUtils {


    /**
     * @Author hangyi
     * @Description 多种日期格式对应的正则表达式
     * @Date 15:10 2020/6/11
     * @Param
     * @return
     **/
    public static String[] dateTimeReg = {
            "\\s*\\d{1,4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\s*",
            "\\s*\\d{1,4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}\\s*",
            "\\s*\\d{1,4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}\\s*",
            "\\s*\\d{1,4}-\\d{1,2}-\\d{1,2}\\s*",
            "\\s*\\d{1,4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\s*",
            "\\s*\\d{1,4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}\\s*",
            "\\s*\\d{1,4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}\\s*",
            "\\s*\\d{1,4}/\\d{1,2}/\\d{1,2}\\s*"
    };
    /**
     * @Author hangyi
     * @Description 多种日期格式
     * @Date 15:10 2020/6/11
     * @Param
     * @return
     **/
    public static DateFormat[] dateFormat = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm"),
            new SimpleDateFormat("yyyy-MM-dd HH"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
            new SimpleDateFormat("yyyy/MM/dd HH:mm"),
            new SimpleDateFormat("yyyy/MM/dd HH"),
            new SimpleDateFormat("yyyy/MM/dd"),
    };

    /**
     * 获取待匹配的字符串对应的正则表达式的下标 index
     *
     * @param dateStr
     * @return
     */
    public static int getRegIndex(String dateStr) {
        Pattern pattern = null;
        int i = 0;
        for (; i < dateTimeReg.length; i++) {
            //compile(正则表达式)
            pattern = Pattern.compile(dateTimeReg[i]);

            //matcher(预匹配的字符串)
            Matcher mat = pattern.matcher(dateStr);

            //find() 是否匹配成功
            boolean isMatch = mat.find();
            if (isMatch) {
                break;
            }
        }

        return i;
    }

    /**
     * @return Wed Jul 22 20:19:16 CST 2020
     * @Author hangyi
     * @Description
     * @Date 15:16 2020/6/11
     * @Param []
     **/
    public static Date getDateTime() {
        return strToDate(dateFormat[0].format(new Date()));

    }

    /**
     * 字符串转日期类:可以转换多种格式的字符串日期
     *
     * @param dateStr
     * @return
     */
    public static Date strToDate(String dateStr) {
        if (dateStr == null || dateStr == "") {
            return null;
        }
        //匹配成功:那么那个i就是我们所需的，目的是为了可以从  dateFormat 取到需要转换为指定的时间格式
        int i = getRegIndex(dateStr.trim());

        Date resultDate = null;
        if (i < dateTimeReg.length) {
            try {
                resultDate = dateFormat[i].parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return resultDate;
    }

    public static Date strToDate1(String dateStr) {
        if (dateStr == null || dateStr == "") {
            return null;
        }
        //匹配成功:那么那个i就是我们所需的，目的是为了可以从  dateFormat 取到需要转换为指定的时间格式
        int i = getRegIndex(dateStr.trim());

        Date resultDate = null;
        if (i < dateTimeReg.length) {
            try {
                resultDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return resultDate;
    }

    /**
     * 日期转字符串
     *
     * @param paramDate
     * @param dateFormat
     * @return
     */
    public static String dateToStr(Date paramDate, String dateFormat) {
        if (paramDate == null || dateFormat == null || dateFormat == "") {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(paramDate);
    }

    /**
     * 获取当前月的第一天: 采用Calendar类实现
     *
     * @return Wed Jul 01 20:19:16 CST 2020
     */
    public static Date getFirstDayInMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得系统当前的时间: 可用于命名一些文件,如上传的图片
     *
     * @return 20200722081916
     * @throws Exception
     * @author
     */
    public static String getCurrentDateStr() throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return 2020-07-22 20:17:33.006
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());

    }

    /**
     * 获取当前时间搓
     *
     * @return 1595420356064
     */
    public static Long getTimeMillis() {
        return System.currentTimeMillis();

    }

    /**
     * 获取月份的天数
     * @param date
     * @return
     */
    public static Integer getDays(Date date){

        Calendar rightNow = Calendar.getInstance();

        rightNow.setTime(date);

        return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取月份日
     * @param date
     * @return
     */
    public static Integer getDay(Date date){

        Calendar rightNow = Calendar.getInstance();

        rightNow.setTime(date);

        return rightNow.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取月
     * @param date
     * @return
     */
    public static Integer getMoth(Date date){

        Calendar rightNow = Calendar.getInstance();

        rightNow.setTime(date);

        return rightNow.get(Calendar.MONTH)+ 1;
    }

    /**
     * 获取年
     * @param date
     * @return
     */
    public static Integer getYear(Date date){

        Calendar rightNow = Calendar.getInstance();

        rightNow.setTime(date);

        return rightNow.get(Calendar.YEAR);
    }

    /**
     * 日期增加年
     * @param date
     * @param years
     * @return 结束日期
     */
    public static Date getEndDate(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    /**
     * 日期增加年
     * @param date
     * @return 结束日期
     */
    public static Date getMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

}
