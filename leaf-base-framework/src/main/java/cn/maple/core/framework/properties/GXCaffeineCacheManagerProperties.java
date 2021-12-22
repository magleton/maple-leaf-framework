package cn.maple.core.framework.properties;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@SuppressWarnings("all")
@Configuration
@PropertySource(value = {"classpath:/${spring.profiles.active}/caffeine-cache-config.yml"}, factory = GXYamlPropertySourceFactory.class, ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "caffeine")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GXCaffeineCacheManagerProperties {
    private Map<String, GXCaffeineCacheProperties> config = new LinkedHashMap<>();
}