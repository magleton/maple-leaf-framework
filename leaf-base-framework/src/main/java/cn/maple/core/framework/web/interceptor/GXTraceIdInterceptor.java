package cn.maple.core.framework.web.interceptor;

import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Component
public class GXTraceIdInterceptor extends GXAuthorizationInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Object requestId = Optional.ofNullable(request.getAttribute(GXTraceIdContextUtils.TRACE_ID_KEY)).orElse(GXTraceIdContextUtils.getTraceId());
        if (Objects.isNull(requestId)) {
            requestId = GXTraceIdContextUtils.generateTraceId();
        }
        String lastRequestId = requestId.toString();
        GXTraceIdContextUtils.setTraceId(lastRequestId);
        request.setAttribute(GXTraceIdContextUtils.TRACE_ID_KEY, lastRequestId);
        response.setHeader(GXTraceIdContextUtils.TRACE_ID_KEY, lastRequestId);
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        GXTraceIdContextUtils.removeTraceId();
    }
}