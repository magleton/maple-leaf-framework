package cn.maple.redisson.properties.nacos;

import cn.maple.redisson.properties.GXRedissonCacheManagerProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
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
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "redisson", dataId = "redisson-cache-config.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXNacosRedissonCacheManagerProperties extends GXRedissonCacheManagerProperties {
    public GXNacosRedissonCacheManagerProperties() {
        log.info("Redisson数据源的配置使用的是NACOS配置");
    }
}