package com.geoxus.core.datasource.properties;

import com.geoxus.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
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
@PropertySource(value = {"classpath:/${spring.profiles.active}/datasource.yml"},
        factory = GXYamlPropertySourceFactory.class,
        ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "dynamic")
//@ConditionalOnMissingClass({"com.alibaba.nacos.api.config.ConfigFactory"})
public class GXDynamicDataSourceProperties {
    protected Map<String, GXDataSourceProperties> datasource = new LinkedHashMap<>();
}
