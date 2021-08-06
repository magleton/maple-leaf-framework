package com.geoxus.commons.config;

import com.geoxus.commons.interceptor.GXCustomMultipartResolver;
import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartResolver;
@Configuration
@PropertySource(value = "classpath:/ymls/${spring.profiles.active}/common.yml",
        factory = GXYamlPropertySourceFactory.class,
        ignoreResourceNotFound = true)
public class GXCommonConfig {
    @Bean
    @ConditionalOnExpression("'${enable-fileupload-progress}'.equals('true')")
    public MultipartResolver multipartResolver() {
        return new GXCustomMultipartResolver();
    }
}
