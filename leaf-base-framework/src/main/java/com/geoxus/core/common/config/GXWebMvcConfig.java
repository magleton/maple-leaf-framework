package com.geoxus.core.common.config;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
    @Component
    @ConfigurationProperties(prefix = "web-mvc")
    @PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/web-mvc.yml"},
            factory = GXYamlPropertySourceFactory.class,
            encoding = "utf-8",
            ignoreResourceNotFound = true)
    public class GXWebMvcConfig {
        private List<String> urlPatterns;

        private List<String> resourcePatterns;
    }