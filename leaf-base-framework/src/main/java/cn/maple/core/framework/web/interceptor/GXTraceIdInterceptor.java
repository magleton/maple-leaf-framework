package cn.maple.core.framework.web.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class GXTraceIdInterceptor extends GXAuthorizationInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String requestId = Optional.ofNullable(request.getHeader(GXTraceIdContextUtils.TRACE_ID_KEY)).orElse(GXTraceIdContextUtils.getTraceId());
        if (CharSequenceUtil.isEmpty(requestId)) {
            requestId = GXTraceIdContextUtils.generateTraceId();
        }
        GXTraceIdContextUtils.setTraceId(requestId);
        request.setAttribute(GXTraceIdContextUtils.TRACE_ID_KEY, requestId);
        response.setHeader(GXTraceIdContextUtils.TRACE_ID_KEY, requestId);
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        GXTraceIdContextUtils.removeTraceId();
    }
}