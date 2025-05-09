package com.wx.genealogy.common.util.wechat;

import com.alibaba.fastjson.JSON;
import com.wx.genealogy.common.util.wechat.prop.WeChatProp;
import com.wx.genealogy.common.util.wechat.util.MyHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Jedis {

    @Autowired
    private MyHttpClient httpClient;

    private static String access_token = "access_token";

    private static String ticket = "ticket";

    public String getTicket() {
        return ticket;
    }

    /**
     * 刷新和获取ticket
     */
    @Scheduled(fixedDelay = 7000 * 1000)
    public static void getaccess_token() {
        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WeChatProp.APP_ID + "&secret=" + WeChatProp.SECRET;
        Map maps = (Map) JSON.parse(MyHttpClient.client(url));
        for (Object map : maps.entrySet()) {
            if (((Map.Entry) map).getKey() == "access_token") {
                access_token = (String) ((Map.Entry) map).getValue();

            }

        }

        //获取ticket
        String url1 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        Map maps1 = (Map) JSON.parse(MyHttpClient.client(url1));
        for (Object map : maps1.entrySet()) {
            if (((Map.Entry) map).getKey() == "ticket") {
                ticket = (String) ((Map.Entry) map).getValue();
            }
        }

    }

}
