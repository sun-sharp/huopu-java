package com.wx.genealogy.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.math.BigDecimal;

public class BigDecimalUtil {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * = null
     */
    public static boolean isNull(BigDecimal bigDecimal) {
        return bigDecimal == null;
    }

    /**
     * = 0
     */
    public static boolean isEqZero(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(ZERO) == 0;
    }

    /**
     * = null OR = 0
     */
    public static boolean isNullOrEqZero(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(ZERO) == 0;
    }

    /**
     * = null OR <0
     */
    public static boolean isNullOrLessZero(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(ZERO) < 0;
    }

    /**
     * = null OR <= 0
     */
    public static boolean isNullOrLessOrEqZero(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(ZERO) <= 0;
    }

    /**
     * > 0
     */
    public static boolean isGreaterThanZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return false;
        }
        return bigDecimal.compareTo(ZERO) > 0;
    }

    /**
     * >= 0
     */
    public static boolean isGreaterThanOrEqualToZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return false;
        }
        return bigDecimal.compareTo(ZERO) >= 0;
    }

    /**
     * 将字符串转换成BigDecimal对象
     */
    public static BigDecimal stringToBigDecimal(String stringBigdecimal) {
        if (StringUtils.isBlank(stringBigdecimal)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(stringBigdecimal).setScale(2, 4);
        } catch (Exception e) {
        }
        return BigDecimal.ZERO;
    }

    /**
     * 将null转换为0
     */
    public static BigDecimal bigDecimalNullToZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        } else {
            return bigDecimal;
        }
    }

    /**
     * 加法运算
     *
     * @param b1 被加数
     * @param b2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        if (isNull(b1) && isNull(b2)){
            return null;
        }
        if (isNull(b1)){
            return b2;
        }
        if (isNull(b2)){
            return b1;
        }
        return b1.add(b2);
    }

    /**
     * 减法运算
     *
     * @param b1 被减数
     * @param b2 减数
     * @param scale 保留scale 位小数
     * @return 两个参数的差
     * @throws IllegalAccessException
     */
    public static BigDecimal sub(BigDecimal b1, BigDecimal b2, int scale){
        if (scale < 0) {
            return null;
        }
        if (isNull(b1)){
            return null;
        }
        if (isNull(b2)){
            b2 = ZERO;
        }
        return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 乘法运算
     *
     * @param b1 被乘数
     * @param b2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal b1, BigDecimal b2) {
        if (isNull(b1) || isNull(b2)){
            return null;
        }
        return b1.multiply(b2);
    }


    /**
     * 乘法运算
     *
     * @param b1 被乘数
     * @param b2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal b1, BigDecimal b2, int scale) {
        if (isNull(b1) || isNull(b2)){
            return null;
        }
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入
     *
     * @param b1 被除数
     * @param b2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal b1, BigDecimal b2) {
        return div(b1, b2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入
     *
     * @param b1    被除数
     * @param b2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商,数据有null，结果就为null
     */
    public static BigDecimal div(BigDecimal b1, BigDecimal b2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (isNull(b1) || isNull(b2)){
            return null;
        }
        if (isEqZero(b2)){
            return null;
        }
        if (isEqZero(b1)){
            return ZERO;
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }
}
