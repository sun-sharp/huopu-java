package com.wx.genealogy.common.util.wechat.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class MyHttpClient {

    /**
     * @return java.lang.String
     * @Author hangyi
     * @Description get
     * @Date 14:47 2020/4/9
     * @Param [url]
     **/
    public static String client(String url) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response1 = template.getForEntity(url, String.class);
        return response1.getBody();
    }

    /**
     * @return java.lang.String
     * @Author hangyi
     * @Description post
     * @Date 14:47 2020/4/9
     * @Param [url, method, string]
     **/
    public static String clientPOSTJSON(String url, String string) {
        RestTemplate template = new RestTemplate();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        HttpEntity<String> request = new HttpEntity<String>(string, headers);
        ResponseEntity<String> response1 = template.postForEntity(url, request, String.class);
        return response1.getBody();
    }
}
