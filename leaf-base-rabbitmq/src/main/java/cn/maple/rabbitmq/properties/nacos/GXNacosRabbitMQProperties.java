package cn.maple.rabbitmq.properties.nacos;

import cn.maple.rabbitmq.properties.GXRabbitMQProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@ConditionalOnClass(name = {"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "rabbit", dataId = "rabbit.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXNacosRabbitMQProperties extends GXRabbitMQProperties {
    public GXNacosRabbitMQProperties() {
        log.info("RabbitMQ数据源配置使用NACOS配置");
    }
}
