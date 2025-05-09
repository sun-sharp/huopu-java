package com.wx.genealogy.common.ssjwt;

import com.alibaba.fastjson.JSON;
import com.wx.genealogy.common.util.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Repository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JwtAccessDeniedHandler
 * @Author hangyi
 * @Data 2020/6/12 16:08
 * @Description
 * @Version 1.0
 **/
@Repository
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSON.toJSONString(ResponseUtil.fail(accessDeniedException.getMessage())));
    }
}
