package cn.maple.core.framework.web.config;

import cn.maple.core.framework.web.interceptor.GXTraceIdInterceptor;
import cn.maple.core.framework.web.interceptor.GXVerifyDeployEnvironmentInterceptor;
import cn.maple.core.framework.web.support.GXRequestHandlerMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * WEB MVC配置
 */
@Configuration
@Slf4j
public class GXWebMvcConfigurer implements WebMvcConfigurer {
    @Resource
    private GXTraceIdInterceptor traceIdInterceptor;

    @Resource
    private GXVerifyDeployEnvironmentInterceptor verifyDeployEnvironmentInterceptor;

    @Resource
    private GXRequestHandlerMethodArgumentResolver requestHandlerMethodArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(false)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyDeployEnvironmentInterceptor);
        registry.addInterceptor(traceIdInterceptor);
        registerCustomerInterceptors(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(requestHandlerMethodArgumentResolver);
        registerCustomerArgumentResolvers(argumentResolvers);
    }

    protected void registerCustomerInterceptors(InterceptorRegistry registry) {
        // TODO document why this method is empty
    }

    protected void registerCustomerArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // TODO document why this method is empty
    }
}
