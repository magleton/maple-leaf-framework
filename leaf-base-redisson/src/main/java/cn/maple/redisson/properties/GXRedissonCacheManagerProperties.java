package cn.maple.redisson.properties;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Configuration
@SuppressWarnings("all")
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "redisson", dataId = "redisson-cache-config.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXRedissonCacheManagerProperties {
    private Map<String, CacheConfig> config = new LinkedHashMap<>();
}
