package com.wx.genealogy.common.ssjwt;

import com.alibaba.fastjson.JSON;
import com.wx.genealogy.common.util.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JwtAuthenticationEntryPoint
 * @Author hangyi
 * @Data 2020/1/20 15:25
 * @Description 解决匿名用户访问无权限资源时的异常
 * @Version 1.0
 **/
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, JSON.toJSONString(ResponseUtil.buildResult(403, "没有凭证")));
    }
}
