package com.geoxus.mongodb.properties;

import com.geoxus.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性
 */
@Data
@SuppressWarnings("all")
@Component
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/mongodb.yml"},
        factory = GXYamlPropertySourceFactory.class,
        ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "mongodb")
@ConditionalOnMissingClass({"com.alibaba.nacos.api.config.ConfigFactory"})
public class GXMongoDynamicDataSourceProperties {
    protected Map<String, GXMongoDataSourceProperties> datasource = new LinkedHashMap<>();
}
