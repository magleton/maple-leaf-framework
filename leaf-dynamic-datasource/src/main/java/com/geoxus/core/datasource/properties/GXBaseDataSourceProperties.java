package com.geoxus.core.datasource.properties;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class GXBaseDataSourceProperties {
    protected Map<String, GXDataSourceProperties> datasource = new LinkedHashMap<>();
}
