package cn.maple.core.framework.web.config;

import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.web.interceptor.GXAuthorizationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Map;

/**
 * WEB MVC配置
 */
@Configuration
@Slf4j
@ConditionalOnBean(value = {RequestMappingHandlerAdapter.class})
public class GXWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(false)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, GXAuthorizationInterceptor> authorizationInterceptor = GXSpringContextUtils.getBeans(GXAuthorizationInterceptor.class);
        authorizationInterceptor.forEach((beanName, interceptor) -> registry.addInterceptor(interceptor));
    }
}
