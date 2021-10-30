package cn.maple.core.framework.interceptor;

import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

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