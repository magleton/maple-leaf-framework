package cn.maple.core.framework.properties;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@SuppressWarnings("all")
@Component
@ConditionalOnExpression("'${spring.cache.type}'.equalsIgnoreCase('caffeine')")
@PropertySource(value = {"classpath:/${spring.profiles.active}/caffeine-cache-config.yml"}, factory = GXYamlPropertySourceFactory.class, ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "caffeine")
public class GXCaffeineCacheManagerProperties {
    private Map<String, GXCaffeineCacheProperties> config = new LinkedHashMap<>();
}