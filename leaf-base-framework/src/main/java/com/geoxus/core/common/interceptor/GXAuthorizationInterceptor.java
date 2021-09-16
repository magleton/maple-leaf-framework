package com.geoxus.core.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前端用户Token验证
 */
@Slf4j
@SuppressWarnings("all")
public abstract class GXAuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.warn("请自定义实现GXAuthorizationInterceptor类");
        return true;
    }
}
