package com.geoxus.core.datasource.properties;

import com.geoxus.core.datasource.factory.GXDSYamlPropertySourceFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 多数据源属性
 */
@Data
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@Component
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/datasource.yml"},
        factory = GXDSYamlPropertySourceFactory.class,
        ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "dynamic")
@ConditionalOnMissingClass({"com.alibaba.nacos.api.config.ConfigFactory"})
public class GXDynamicDataSourceProperties extends GXBaseDataSourceProperties {

}
