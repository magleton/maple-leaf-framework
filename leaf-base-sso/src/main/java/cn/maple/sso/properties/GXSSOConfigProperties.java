package cn.maple.sso.properties;

import cn.maple.sso.config.GXSSOConfig;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NacosPropertySource(dataId = "sso.yml", autoRefreshed = true, groupId = "DEFAULT_GROUP")
@NacosConfigurationProperties(prefix = "sso", dataId = "sso.yml", autoRefreshed = true)
public class GXSSOConfigProperties {
    @NestedConfigurationProperty
    private GXSSOConfig config;
}
