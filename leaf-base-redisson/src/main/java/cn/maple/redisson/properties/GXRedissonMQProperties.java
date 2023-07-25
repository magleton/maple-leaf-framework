package cn.maple.redisson.properties;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public abstract class GXRedissonMQProperties {
    public Map<String, GXRedissonConnectProperties> getConfig() {
        return new HashMap<>();
    }
}
