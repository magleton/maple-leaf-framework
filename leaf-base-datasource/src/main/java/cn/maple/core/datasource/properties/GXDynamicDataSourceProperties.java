package cn.maple.core.datasource.properties;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性
 */
@Data
@SuppressWarnings("all")
@Component
@NacosConfigurationProperties(groupId = "DEFAULT_GROUP", prefix = "dynamic", dataId = "datasource.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXDynamicDataSourceProperties {
    protected Map<String, GXDataSourceProperties> datasource = new LinkedHashMap<>();
}
