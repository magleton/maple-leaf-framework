package cn.maple.core.datasource.properties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性
 */
public abstract class GXDynamicDataSourceProperties {
    protected Map<String, GXDataSourceProperties> datasource = new LinkedHashMap<>();

    public Map<String, GXDataSourceProperties> getDatasource() {
        return datasource;
    }
}
