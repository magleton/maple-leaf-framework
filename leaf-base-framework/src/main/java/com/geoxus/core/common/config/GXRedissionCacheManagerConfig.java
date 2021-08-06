package com.geoxus.core.common.config;

import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Configuration
@SuppressWarnings("all")
@ConditionalOnClass(name = {"org.redisson.Redisson"})
@ConditionalOnMissingClass(value = {"com.alibaba.nacos.api.config.ConfigFactory"})
@PropertySource(value = {"classpath:/redisson-cache-config.yml"},
        factory = GXYamlPropertySourceFactory.class,
        encoding = "utf-8",
        ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "redission")
public class GXRedissionCacheManagerConfig {
    private Map<String, CacheConfig> config = new LinkedHashMap<>();
}
