package cn.maple.mongodb.datasource.properties.local;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import cn.maple.mongodb.datasource.properties.GXMongoDynamicDataSourceProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 多数据源属性
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Component
@ConditionalOnMissingClass({"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@PropertySource(value = {"classpath:/${spring.profiles.active}/mongodb.yml"}, factory = GXYamlPropertySourceFactory.class, ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "mongodb")
public class GXLocalMongoDynamicDataSourceProperties extends GXMongoDynamicDataSourceProperties {
    public GXLocalMongoDynamicDataSourceProperties() {
        log.info("MongoDB数据源的配置使用的是本地配置");
    }
}
