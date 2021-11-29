package cn.maple.sso.properties;

import cn.maple.sso.config.GXSSOConfig;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "sso", dataId = "sso.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXSSOConfigProperties {
    @NestedConfigurationProperty
    private GXSSOConfig config;
}
