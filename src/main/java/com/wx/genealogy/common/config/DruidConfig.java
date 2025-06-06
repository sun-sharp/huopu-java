package com.wx.genealogy.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: HangYi
 * @Date: 2021/3/29 15:52
 */
@Configuration
public class DruidConfig {
    /**
     * 该注解向bean自动注入对应的属性，属性在配置文件配置
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    //配置druid的监控
    /**
     * 1.配置管理后台的servlet
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        //druid监控页面的url
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map initParams = new HashMap<>();

        //登陆用户名
        initParams.put("loginUsername","weixing");
        //密码
        initParams.put("loginPassword","wx123456");
        //白名单
        initParams.put("allow","");
        //黑名单
        initParams.put("deny","");
        bean.setInitParameters(initParams);
        return bean;
    }
    /**
     * 2.配置一个web监控的filter,监控sql
     */
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,*.html,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
}
