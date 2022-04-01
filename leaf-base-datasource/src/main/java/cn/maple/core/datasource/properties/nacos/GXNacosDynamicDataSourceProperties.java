package cn.maple.core.datasource.properties.nacos;

import cn.maple.core.datasource.properties.GXDataSourceProperties;
import cn.maple.core.datasource.properties.GXDynamicDataSourceProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性
 */
@Data
@Slf4j
@Component
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@ConditionalOnClass(name = {"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "dynamic", dataId = "datasource.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXNacosDynamicDataSourceProperties extends GXDynamicDataSourceProperties {
    private Map<String, GXDataSourceProperties> datasource = new LinkedHashMap<>();

    public GXNacosDynamicDataSourceProperties() {
        log.info("MySQL数据源的配置使用的是NACOS配置");
    }

    @Override
    public Map<String, GXDataSourceProperties> getDatasource() {
        return datasource;
    }
}
