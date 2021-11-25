package cn.maple.redisson.properties;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Configuration
@SuppressWarnings("all")
@ConditionalOnClass(name = {"org.redisson.Redisson"})
@NacosPropertySource(dataId = "redisson-cache-config.yml", autoRefreshed = true, groupId = "DEFAULT_GROUP")
@NacosConfigurationProperties(prefix = "redisson", dataId = "redisson-cache-config.yml", autoRefreshed = true)
public class GXRedissonCacheManagerProperties {
    private Map<String, CacheConfig> config = new LinkedHashMap<>();
}
