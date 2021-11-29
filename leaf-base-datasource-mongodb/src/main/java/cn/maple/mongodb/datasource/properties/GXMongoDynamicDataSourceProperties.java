package cn.maple.mongodb.datasource.properties;

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
@NacosConfigurationProperties(groupId = "${nacos.config.group}", prefix = "mongodb", dataId = "mongodb.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXMongoDynamicDataSourceProperties {
    protected Map<String, GXMongoDataSourceProperties> datasource = new LinkedHashMap<>();
}
