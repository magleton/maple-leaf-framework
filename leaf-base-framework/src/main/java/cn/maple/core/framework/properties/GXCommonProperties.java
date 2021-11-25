package cn.maple.core.framework.properties;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@SuppressWarnings("all")
@NacosPropertySource(dataId = "common.yml", autoRefreshed = true, groupId = "DEFAULT_GROUP")
@NacosConfigurationProperties(dataId = "common.yml", autoRefreshed = true)
public class GXCommonProperties {
}
