package com.geoxus.core.common.web;

import com.geoxus.core.common.interceptor.GXAuthorizationInterceptor;
import com.geoxus.core.common.interceptor.GXRequestHandlerMethodArgumentResolver;
import com.geoxus.core.common.interceptor.GXTraceIdInterceptor;
import com.geoxus.core.common.support.GXHandlerMethodArgumentResolver;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.common.web.interceptor.GXBaseSSOPermissionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * WEB MVC配置
 */
@Configuration
@Slf4j
public class GXWebMvcInterceptorConfig implements WebMvcConfigurer {
    @Resource
    private GXRequestHandlerMethodArgumentResolver requestToBeanHandlerMethodArgumentResolver;

    @Resource
    private GXWebMvcConfig webMvcConfig;

    @Resource
    private GXTraceIdInterceptor traceIdInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        final List<String> list = webMvcConfig.getUrlPatterns();
        registry.addInterceptor(traceIdInterceptor);
        if (Objects.nonNull(GXSpringContextUtils.getBean(GXAuthorizationInterceptor.class))) {
            registry.addInterceptor(Objects.requireNonNull(GXSpringContextUtils.getBean(GXAuthorizationInterceptor.class))).addPathPatterns(list);
        }
        if (Objects.nonNull(GXSpringContextUtils.getBean(GXBaseSSOPermissionInterceptor.class))) {
            registry.addInterceptor(Objects.requireNonNull(GXSpringContextUtils.getBean(GXBaseSSOPermissionInterceptor.class))).addPathPatterns(list);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(requestToBeanHandlerMethodArgumentResolver);
        if (Objects.nonNull(GXSpringContextUtils.getBean(GXHandlerMethodArgumentResolver.class))) {
            argumentResolvers.add(GXSpringContextUtils.getBean(GXHandlerMethodArgumentResolver.class));
        }
    }
}
