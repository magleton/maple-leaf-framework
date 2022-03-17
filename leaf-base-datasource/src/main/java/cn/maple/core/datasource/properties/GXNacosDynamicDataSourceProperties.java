package cn.maple.core.datasource.properties;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * 多数据源属性
 */
@Data
@Slf4j
@Component
@EqualsAndHashCode(callSuper = true)
@ConditionalOnClass(name = {"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "dynamic", dataId = "datasource.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXNacosDynamicDataSourceProperties extends GXDynamicDataSourceProperties {
    public GXNacosDynamicDataSourceProperties() {
        log.info("数据源的配置使用的是Nacos配置");
    }
}
