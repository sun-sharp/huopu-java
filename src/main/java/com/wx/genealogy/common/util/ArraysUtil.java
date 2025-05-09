package com.wx.genealogy.common.util;

/**
 * @ClassName ArraysUtil
 * @Author hangyi
 * @Data 2020/8/18 10:38
 * @Description 数组工具类
 * @Version 1.0
 **/
public class ArraysUtil {

    /**
     * @return boolean
     * @Author hangyi
     * @Description 判断数组中是否存在元素
     * @Date 10:38 2020/8/18
     * @Param [arr, containValue]
     **/
    public static boolean useLoop(String[] arr, String containValue) {
        //判断是否为空
        if (arr == null || arr.length == 0) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            //all null
            if (containValue != null && containValue.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }


}
