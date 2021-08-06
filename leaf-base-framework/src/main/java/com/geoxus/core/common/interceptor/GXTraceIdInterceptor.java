package com.geoxus.core.common.interceptor;

import com.geoxus.core.common.util.GXTraceIdContextUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GXTraceIdInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        GXTraceIdContextUtils.setTraceId(GXTraceIdContextUtils.GXTraceIdGenerator.getTraceId());
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        GXTraceIdContextUtils.clearTraceId();
    }
}