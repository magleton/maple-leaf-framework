package cn.maple.core.datasource.properties.nacos;

import cn.maple.core.datasource.properties.GXDataSourceProperties;
import cn.maple.core.datasource.properties.GXDynamicDataSourceProperties;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@NacosPropertySource(dataId = "datasource.yml",
        groupId = "${spring.cloud.nacos.config.group:${nacos.config.group:}}",
        properties = @NacosProperties(
                serverAddr = "${spring.cloud.nacos.config.server-addr:${nacos.config.server-addr:}}",
                namespace = "${spring.cloud.nacos.config.namespace:${nacos.config.namespace:}}",
                username = "${spring.cloud.nacos.username:${nacos.config.username:}}",
                password = "${spring.cloud.nacos.password:${nacos.config.password:}}"))
@ConfigurationProperties(prefix = "dynamic")
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
