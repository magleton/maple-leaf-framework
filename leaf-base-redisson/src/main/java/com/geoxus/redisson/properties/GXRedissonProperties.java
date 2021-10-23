package com.geoxus.redisson.properties;

import com.geoxus.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Configuration
@SuppressWarnings("all")
@Component
@ConditionalOnClass(name = {"org.redisson.Redisson"})
@ConditionalOnMissingClass(value = {"com.alibaba.nacos.api.config.ConfigFactory"})
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/redisson.yml"},
        factory = GXYamlPropertySourceFactory.class,
        encoding = "utf-8",
        ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "redisson")
public class GXRedissonProperties {
    private Map<String, GXRedissonConnectProperties> config = new LinkedHashMap<>();
}
