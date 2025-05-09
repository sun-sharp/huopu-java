package com.wx.genealogy.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * @Author hangyi
 * @Description 静态资源配置
 * @Date 15:40 2020/5/26
 * @Param
 * @return
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * @return void
     * @Author hangyi
     * @Description 自定义资源映射目录，
     * addResourceHandler：指的是对外暴露的访问路径
     * addResourceLocations：指的是内部文件放置的目录
     * @Date 15:41 2020/5/26
     * @Param [registry]
     **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + new File("").getAbsolutePath() + "/img/");
    }
}
