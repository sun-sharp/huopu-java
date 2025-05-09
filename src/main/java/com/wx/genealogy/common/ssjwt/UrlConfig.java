package com.wx.genealogy.common.ssjwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * @ClassName UrlConfig
 * @Author hangyi
 * @Data 2020/7/3 17:38
 * @Description
 * @Version 1.0
 **/
@Configuration
public class UrlConfig {
    //允许多请求地址多加斜杠  比如 /msg/list   //msg/list
    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }
}
