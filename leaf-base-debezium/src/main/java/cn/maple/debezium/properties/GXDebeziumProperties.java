package cn.maple.debezium.properties;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public abstract class GXDebeziumProperties {
    public Map<String, String> getConfig() {
        return new HashMap<>();
    }
}
