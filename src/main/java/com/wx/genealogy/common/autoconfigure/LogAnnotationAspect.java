package com.wx.genealogy.common.autoconfigure;

import com.alibaba.fastjson.JSON;
import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.ssjwt.SecurityUtil;
import com.wx.genealogy.common.util.CusAccessObjectUtil;
import com.wx.genealogy.common.util.HttpContextUtils;
import com.wx.genealogy.system.entity.SysLog;
import com.wx.genealogy.system.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @ClassName LogAnnotationAspect
 * @Author hangyi
 * @Data 2020/6/11 12:00
 * @Description 系统日志，切面处理类
 * @Version 1.0
 **/
@Aspect
@Component
public class LogAnnotationAspect {

    @Autowired
    private SysLogService sysLogService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation( com.wx.genealogy.common.annotation.MyLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        //保存日志
        SysLog sysLog = new SysLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            String value = myLog.value();
            //保存获取的操作
            sysLog.setOperation(value);
        }

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
        String params = "";
        try {
            params = JSON.toJSONString(args);
        } catch (Exception e) {

        }
        if (params.length() < 500) {
            sysLog.setParams(params);
        } else {
            sysLog.setParams("");
        }

        sysLog.setCreateTime(LocalDateTime.now());
        //获取用户名
        sysLog.setUserName(SecurityUtil.getUserName());

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //获取用户ip地址
        sysLog.setIp(CusAccessObjectUtil.getIpAddress(request));

        //调用service保存SysLog实体类到数据库
        sysLogService.insertLog(sysLog);
    }
}
