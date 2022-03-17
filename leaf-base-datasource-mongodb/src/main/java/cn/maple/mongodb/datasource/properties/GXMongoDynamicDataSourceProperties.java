package cn.maple.mongodb.datasource.properties;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GXMongoDynamicDataSourceProperties {
    protected Map<String, GXMongoDataSourceProperties> datasource = new LinkedHashMap<>();

    public Map<String, GXMongoDataSourceProperties> getDatasource() {
        return datasource;
    }
}
