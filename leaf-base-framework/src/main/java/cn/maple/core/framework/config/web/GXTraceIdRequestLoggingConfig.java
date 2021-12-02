package cn.maple.core.framework.config.web;

import cn.maple.core.framework.filter.GXTraceIdRequestLoggingFilter;

//@Configuration
public class GXTraceIdRequestLoggingConfig {
    //@Bean
    public GXTraceIdRequestLoggingFilter traceIdRequestLoggingFilter() {
        return new GXTraceIdRequestLoggingFilter();
    }
}