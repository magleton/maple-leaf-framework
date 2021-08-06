package com.geoxus.core.common.filter;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.util.GXTraceIdContextUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class GXTraceIdRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override
    protected void beforeRequest(HttpServletRequest request, @NotNull String message) {
        String requestId = request.getHeader(GXTraceIdContextUtils.TRACE_ID_KEY);
        if (CharSequenceUtil.isNotEmpty(requestId)) {
            GXTraceIdContextUtils.setTraceId(requestId);
        } else {
            GXTraceIdContextUtils.setTraceId(GXTraceIdContextUtils.GXTraceIdGenerator.getTraceId());
        }
    }

    @Override
    protected void afterRequest(@NotNull HttpServletRequest request, @NotNull String message) {
        GXTraceIdContextUtils.removeTraceId();
    }
}
