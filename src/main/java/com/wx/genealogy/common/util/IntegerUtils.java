package com.wx.genealogy.common.util;

public class IntegerUtils {

    /**
     * 加法运算
     *
     * @param i1 被加数
     * @param i2 加数
     * @return 两个参数的和
     */
    public static Integer add(Integer i1, Integer i2) {
        if (i1 == null && i2 == null){
            return null;
        }
        if (i1 == null){
            return i2;
        }
        if (i2 == null){
            return i1;
        }
        return i1 + i2;
    }
}
