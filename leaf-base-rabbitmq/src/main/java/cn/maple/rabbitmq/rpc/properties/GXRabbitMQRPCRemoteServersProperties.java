package cn.maple.rabbitmq.rpc.properties;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@NacosPropertySource(groupId = "${nacos.config.group:DEFAULT_GROUP}", dataId = "rabbit.yml", autoRefreshed = true, type = ConfigType.YAML)
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXRabbitMQRPCRemoteServersProperties {
    private Map<String, Map<String, Object>> servers = new HashMap<>();
}
