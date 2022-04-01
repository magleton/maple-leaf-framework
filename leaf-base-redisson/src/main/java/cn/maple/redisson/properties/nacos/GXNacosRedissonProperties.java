package cn.maple.redisson.properties.nacos;

import cn.maple.redisson.properties.GXRedissonConnectProperties;
import cn.maple.redisson.properties.GXRedissonProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Slf4j
@Component
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@ConditionalOnClass(name = {"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "redisson", dataId = "redisson.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXNacosRedissonProperties extends GXRedissonProperties {
    private Map<String, GXRedissonConnectProperties> config = new LinkedHashMap<>();

    public GXNacosRedissonProperties() {
        log.info("Redisson数据源的配置使用的是NACOS配置");
    }

    @Override
    public Map<String, GXRedissonConnectProperties> getConfig() {
        return config;
    }
}
