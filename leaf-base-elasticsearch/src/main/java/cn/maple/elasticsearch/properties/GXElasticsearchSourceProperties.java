package cn.maple.elasticsearch.properties;

import java.util.HashMap;
import java.util.Map;

public abstract class GXElasticsearchSourceProperties {
    public Map<String, GXElasticsearchProperties> getDatasource() {
        return new HashMap<>();
    }
}
