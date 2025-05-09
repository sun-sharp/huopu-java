package com.wx.genealogy.common.util.wechat.util;

import com.alibaba.fastjson.JSON;
import com.wx.genealogy.common.util.wechat.prop.WeChatProp;

import java.util.Map;

/**
 * @ClassName WeChatUtil
 * @Author hangyi
 * @Data 2020/4/9 14:17
 * @Description
 * @Version 1.0
 **/
public class WeChatUtil {
    /**
     * @return java.lang.String
     * @Author hangyi
     * @Description
     * @Date 14:37 2020/4/9
     * @Param [s:接口名]
     **/
    public static String getCode() {
        return WeChatProp.WEIXIN_OPEN_URL + "/connect/oauth2/authorize?appid=" + WeChatProp.APP_ID + "&redirect_uri=" + WeChatProp.MY_URL +
                "/getToken&response_type=code&scope=snsapi_userinfo#wechat_redirect";
    }

    public static Map getToken(String code) {
        String url = WeChatProp.WEIXIN_API_URL + "/sns/oauth2/access_token?appid=" + WeChatProp.APP_ID + "&secret=" + WeChatProp.SECRET +
                "&code=" + code + "&grant_type=authorization_code";
        Map maps = (Map) JSON.parse(MyHttpClient.client(url));
        return maps;
    }

    public static Map getUserInfo(String access_token, String openid) {
        String url = WeChatProp.WEIXIN_API_URL + "/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        Map maps = (Map) JSON.parse(MyHttpClient.client(url));
        return maps;
    }
}
