package com.wx.genealogy.common.annotation;

import java.lang.annotation.*;

/**
 * @Author hangyi
 * @Description 日志注解
 * @Date 11:55 2020/6/11
 * @Param
 * @return
 **/
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface MyLog {
    String value() default "";
}
