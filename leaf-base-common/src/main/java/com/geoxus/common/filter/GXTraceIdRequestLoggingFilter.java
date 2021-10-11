package com.geoxus.common.filter;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.common.util.GXTraceIdContextUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

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