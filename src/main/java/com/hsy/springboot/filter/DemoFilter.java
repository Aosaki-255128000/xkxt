package com.hsy.springboot.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {

    @Override   // 初始化方法
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init 初始化执行");
    }

    @Override   // 每次拦截到请求都会调用
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("拦截到了某个请求(放行前)");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("拦截到了某个请求(放行后)");
    }

    @Override   // 销毁方法
    public void destroy() {
        System.out.println("destroy 销毁执行");
    }
}
