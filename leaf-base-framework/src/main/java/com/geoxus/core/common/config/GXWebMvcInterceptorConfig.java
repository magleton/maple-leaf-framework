package com.geoxus.core.common.config;

import com.geoxus.core.common.interceptor.GXAuthorizationInterceptor;
import com.geoxus.core.common.interceptor.GXLoginUserHandlerMethodArgumentResolver;
import com.geoxus.core.common.interceptor.GXRequestToBeanHandlerMethodArgumentResolver;
import com.geoxus.core.common.interceptor.GXTraceIdInterceptor;
import com.geoxus.core.common.util.GXSpringContextUtils;
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
    private GXRequestToBeanHandlerMethodArgumentResolver requestToBeanHandlerMethodArgumentResolver;

    @Resource
    private GXLoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Resource
    private GXWebMvcConfig gxWebMvcConfig;

    @Resource
    private GXTraceIdInterceptor gxTraceIdInterceptor;

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
        final List<String> list = gxWebMvcConfig.getUrlPatterns();
        registry.addInterceptor(gxTraceIdInterceptor);
        if (Objects.nonNull(GXSpringContextUtils.getBean(GXAuthorizationInterceptor.class))) {
            registry.addInterceptor(Objects.requireNonNull(GXSpringContextUtils.getBean(GXAuthorizationInterceptor.class))).addPathPatterns(list);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(requestToBeanHandlerMethodArgumentResolver);
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }
}
