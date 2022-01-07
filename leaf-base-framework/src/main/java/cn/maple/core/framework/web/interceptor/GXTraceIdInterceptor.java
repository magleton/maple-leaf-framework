package cn.maple.core.framework.web.interceptor;

import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class GXTraceIdInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Object requestId = Optional.ofNullable(request.getAttribute(GXTraceIdContextUtils.TRACE_ID_KEY)).orElse(GXTraceIdContextUtils.getTraceId());
        response.setHeader(GXTraceIdContextUtils.TRACE_ID_KEY, requestId.toString());
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        GXTraceIdContextUtils.removeTraceId();
    }
}
