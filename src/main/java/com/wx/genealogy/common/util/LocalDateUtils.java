package com.wx.genealogy.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class LocalDateUtils {

//    //构造日期
//        LocalDate.of(2020,06,05);
//    //构造日期
//        LocalDate.parse("2020-06-05");
//
//    //计算已过去的日期
//		LocalDate.now().minusDays(1);//昨天
//		LocalDate.now().minusWeeks(1);//上周
//		LocalDate.now().minusMonths(1);//上个月
//		LocalDate.now().minusYears(1)//上一年
//
//    //计算将来的日期（加法）
//		LocalDate.now().plusDays(1);//明天
//		LocalDate.now().plusWeeks(1);//下周
//		LocalDate.now().plusMonths(1);//下个月的今天
//		LocalDate.now().plusYears(1);//明年
//
//    //计算相差天数，切记不要使用Period.between()[因为跨月的话天数会重置，还要自己去计算月份，甚至年份]   要用 ChronoUnit.DAYS.between
//    LocalDate date = LocalDate.of(2020,06,05);
//    //当天日期
//    LocalDate nowDate = LocalDate.now();
//    //计算2020-06-05 距离今天相差多少天
//
//        ChronoUnit.DAYS.between(date, nowDate);

    /**
     * 字符串转LocalDate
     * @param dateStr 时间字符串
     * @param dateTimeFormatter 时间字符串样式
     * @return
     */
    public static LocalDate strToLocalDate(String dateStr, String dateTimeFormatter){
        //指定转换格式
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateTimeFormatter);
        //进行转换
        return LocalDate.parse(dateStr, fmt);
    }

    /**
     * 获取日期的前一年
     * @param localDate
     * @return
     */
    public static LocalDate getMinusOneYears(LocalDate localDate){
        return getMinusNYears(localDate, 1);
    }

    /**
     * 获取日期的前n年
     * @param localDate
     * @param n
     * @return
     */
    public static LocalDate getMinusNYears(LocalDate localDate, Integer n){
        LocalDate date = localDate.minusYears(n);
        if (localDate.getDayOfMonth() == localDate.lengthOfMonth()){
            date = getLastDayOfMonth(date);
        }
        return date;
    }

    /**
     * 获取日期的前一月
     * @param localDate
     * @return
     */
    public static LocalDate getMinusOneMonths(LocalDate localDate){
        return getMinusNMonths(localDate, 1);
    }

    /**
     * 获取日期的前n月
     * @param localDate
     * @return
     */
    public static LocalDate getMinusNMonths(LocalDate localDate, Integer n){
        LocalDate date = localDate.minusMonths(n);
        if (localDate.getDayOfMonth() == localDate.lengthOfMonth()){
            date = getLastDayOfMonth(date);
        }
        return date;
    }

    /**
     * 获取年第一天
     * @param localDate
     * @return
     */
    public static LocalDate getFirstDayOfYear(LocalDate localDate){
        return localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取年最后一天
     * @param localDate
     * @return
     */
    public static LocalDate getLastDayOfYear(LocalDate localDate){
        return localDate.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 获取月第一天
     * @param localDate
     * @return
     */
    public static LocalDate getFirstDayOfMonth(LocalDate localDate){
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取月最后一天
     * @param localDate
     * @return
     */
    public static LocalDate getLastDayOfMonth(LocalDate localDate){
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取月第n天日期
     * @param localDate
     * @param n
     * @return
     */
    public static LocalDate getNDayOfMonth(LocalDate localDate, Integer n){
        return localDate.withDayOfMonth(n);
    }

    /**
     * 获取年第n天日期
     * @param localDate
     * @param n
     * @return
     */
    public static LocalDate getNDayOfYear(LocalDate localDate, Integer n){
        return localDate.withDayOfYear(n);
    }

    /**
     * 获得得到日期前一年最近的同一个星期日
     * @param localDate
     * @return
     */
    public static LocalDate getMinusOneYearIsDayOfWeekDay(LocalDate localDate){
        String dayOfWeek = String.valueOf(localDate.getDayOfWeek());
        LocalDate minusOneYearDate = getMinusOneYears(localDate);
        String minusOneYearDateDayOfWeek = String.valueOf(minusOneYearDate.getDayOfWeek());
        if (dayOfWeek.equals(minusOneYearDateDayOfWeek)){
            return minusOneYearDate;
        }else {
            LocalDate minusDate = minusOneYearDate.minusDays(1);
            LocalDate plusDate = minusOneYearDate.plusDays(1);
            while (true){
                if (String.valueOf(minusDate.getDayOfWeek()).equals(dayOfWeek)){
                    return minusDate;
                }
                if (String.valueOf(plusDate.getDayOfWeek()).equals(dayOfWeek)){
                    return plusDate;
                }
                minusDate = minusDate.minusDays(1);
                plusDate = plusDate.plusDays(1);
            }
        }
    }
}
