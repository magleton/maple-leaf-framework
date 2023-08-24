package cn.maple.seata.properties;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 每个应用自定义的环境配置文件
 */
@Data
@Slf4j
@Component
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@ConditionalOnMissingClass({"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@PropertySource(value = {"classpath:/${spring.profiles.active}/seata.yml"}, factory = GXYamlPropertySourceFactory.class, ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "")
public class GXLocalSeataProperties {
}
