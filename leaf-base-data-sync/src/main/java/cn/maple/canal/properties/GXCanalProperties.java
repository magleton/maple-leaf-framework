package cn.maple.canal.properties;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@SuppressWarnings("all")
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "canal", dataId = "canal.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXCanalProperties {
    /**
     * RABBITMQ 消费CANAL的线程数量
     */
    private final String concurrencyCount = "10";

    /**
     * canal的queue的名字
     * <p>
     * 监听的队列名字
     */
    private String canalQueueName = "canalQueue";

    /**
     * 交换机的名字
     * 对应 conf/canal.properties文件里面配置的 rabbitmq.exchange = exchange.fanout.canal
     */
    private String exchangeName = "exchange.fanout.canal";

    /**
     * 交换机的名字
     * 对应 conf/example/instance.properties文件里面配置的  canal.mq.topic = canal.example.exchange.routingkey
     */
    private String routingKey = "canal.example.exchange.routingkey";

}
