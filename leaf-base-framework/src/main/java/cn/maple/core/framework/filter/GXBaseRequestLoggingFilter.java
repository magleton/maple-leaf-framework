package cn.maple.core.framework.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class GXBaseRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override
    protected void beforeRequest(HttpServletRequest request, @NotNull String message) {
        String requestId = Optional.ofNullable(request.getHeader(GXTraceIdContextUtils.TRACE_ID_KEY)).orElse(GXTraceIdContextUtils.getTraceId());
        if (CharSequenceUtil.isEmpty(requestId)) {
            requestId = GXTraceIdContextUtils.generateTraceId();
        }
        GXTraceIdContextUtils.setTraceId(requestId);
        request.setAttribute(GXTraceIdContextUtils.TRACE_ID_KEY, requestId);
    }

    @Override
    protected void afterRequest(@NotNull HttpServletRequest request, @NotNull String message) {
        GXTraceIdContextUtils.removeTraceId();
    }
}
