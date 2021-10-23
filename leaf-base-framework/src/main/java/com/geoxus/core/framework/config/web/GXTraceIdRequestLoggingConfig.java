package com.geoxus.core.framework.config.web;

import com.geoxus.core.framework.filter.GXTraceIdRequestLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GXTraceIdRequestLoggingConfig {
    @Bean
    public GXTraceIdRequestLoggingFilter traceIdRequestLoggingFilter() {
        return new GXTraceIdRequestLoggingFilter();
    }
}