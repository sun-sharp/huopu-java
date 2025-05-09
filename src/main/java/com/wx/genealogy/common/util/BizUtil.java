package com.wx.genealogy.common.util;

import lombok.Data;

import java.util.Random;

/**
 * @ClassName BizUtil
 * @Author hangyi
 * @Data 2020/7/15 15:49
 * @Description
 * @Version 1.0
 **/
@Data
public class BizUtil {

    public static String getOrderIdByTime() throws Exception {
        String newDate = DateUtils.getCurrentDateStr();
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

    public static String getRankingStr(Integer ranking) {
        if(ranking != null) {
            if(ranking == 1) return "老大";
            else if(ranking == 2) return "老二";
            else if(ranking == 3) return "老三";
            else if(ranking == 4) return "老四";
            else if(ranking == 5) return "老五";
            else if(ranking == 6) return "老六";
            else if(ranking == 7) return "老七";
            else if(ranking == 8) return "老八";
            else if(ranking == 9) return "老九";
        }
        return "";
    }
}
