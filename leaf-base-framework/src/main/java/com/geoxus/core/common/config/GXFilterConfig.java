package com.geoxus.core.common.config;

import com.geoxus.core.common.filter.GXXssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

@Configuration
public class GXFilterConfig {
    @Bean
    public FilterRegistrationBean<GXXssFilter> xssFilterRegistration() {
        FilterRegistrationBean<GXXssFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new GXXssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
}
