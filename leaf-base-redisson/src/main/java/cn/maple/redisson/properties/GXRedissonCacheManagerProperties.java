package cn.maple.redisson.properties;

import org.redisson.spring.cache.CacheConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GXRedissonCacheManagerProperties {
    protected Map<String, CacheConfig> config = new LinkedHashMap<>();

    public Map<String, CacheConfig> getConfig() {
        return config;
    }
}
