package cn.maple.redisson.properties.nacos;

import cn.maple.redisson.properties.GXRedissonCacheManagerProperties;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Component
@ConditionalOnClass(name = {"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@NacosPropertySource(dataId = "redisson-cache-config.yml",
        groupId = "${spring.cloud.nacos.config.group:${nacos.config.group:}}",
        properties = @NacosProperties(
                serverAddr = "${spring.cloud.nacos.config.server-addr:${nacos.config.server-addr:}}",
                namespace = "${spring.cloud.nacos.config.namespace:${nacos.config.namespace:}}",
                username = "${spring.cloud.nacos.username:${nacos.config.username:}}",
                password = "${spring.cloud.nacos.password:${nacos.config.password:}}"))
//@ConfigurationProperties(prefix = "redisson")
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "redisson", dataId = "redisson-cache-config.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXNacosRedissonCacheManagerProperties extends GXRedissonCacheManagerProperties {
    public GXNacosRedissonCacheManagerProperties() {
        log.info("Redisson数据源的配置使用的是NACOS配置");
    }
}
