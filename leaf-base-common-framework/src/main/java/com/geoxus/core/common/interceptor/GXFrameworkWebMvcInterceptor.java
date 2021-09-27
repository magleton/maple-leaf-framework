package com.geoxus.core.common.interceptor;

import com.geoxus.common.web.interceptor.GXWebMvcInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Slf4j
public class GXFrameworkWebMvcInterceptor extends GXWebMvcInterceptor {
    @Resource
    private GXTraceIdInterceptor traceIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(traceIdInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }
}
