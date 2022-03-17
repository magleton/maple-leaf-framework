package cn.maple.redisson.properties;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GXRedissonProperties {
    protected Map<String, GXRedissonConnectProperties> config = new LinkedHashMap<>();

    public Map<String, GXRedissonConnectProperties> getConfig() {
        return config;
    }
}
