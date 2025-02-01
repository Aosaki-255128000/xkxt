package com.hsy.springboot.filter;

import com.alibaba.fastjson.JSONObject;
import com.hsy.springboot.common.Result;
import com.hsy.springboot.utils.JWTUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/login")
@Order(1)
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取请求url
        String url = request.getRequestURL().toString();
        log.info("请求的url: {}", url);

        // 2. 判断请求url是否包含login，放行登录操作
        if(url.contains("login")) {
            log.info("登录操作，放行");
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        // 3. 获取请求头中的令牌token
        String jwt = request.getHeader("token");

        // 4. 判断令牌是否存在
        if(!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空，返回未登录的信息");
            Result error = Result.errorPrint("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return;
        }

        // 5. 解析token，若解析失败，返回未登录错误结果
        try {
            JWTUtils.verifyToken(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            Result error = Result.errorPrint("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return;
        }


        // 6. 放行
        log.info("令牌合法，放行");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
