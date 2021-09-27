package com.geoxus.core.common.web;

import com.geoxus.core.common.filter.GXTraceIdRequestLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GXTraceIdRequestLoggingConfig {
    @Bean
    public GXTraceIdRequestLoggingFilter traceIdRequestLoggingFilter() {
        return new GXTraceIdRequestLoggingFilter();
    }
}