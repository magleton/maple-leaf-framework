package cn.maple.redisson.properties;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Configuration
@SuppressWarnings("all")
@Component
@ConditionalOnClass(name = {"org.redisson.Redisson"})
@NacosPropertySource(dataId = "redisson.yml", autoRefreshed = true, groupId = "DEFAULT_GROUP")
@NacosConfigurationProperties(prefix = "redisson", dataId = "redisson.yml", autoRefreshed = true)
public class GXRedissonProperties {
    private Map<String, GXRedissonConnectProperties> config = new LinkedHashMap<>();
}
