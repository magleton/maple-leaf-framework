package cn.maple.rabbitmq.properties;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@SuppressWarnings("all")
@NacosConfigurationProperties(groupId = "${nacos.config.group:DEFAULT_GROUP}", prefix = "rabbit", dataId = "rabbit.yml", autoRefreshed = true, type = ConfigType.YAML)
public class GXRabbitMQProperties {
    /**
     * 确认模式
     */
    private CachingConnectionFactory.ConfirmType publisherConfirmType;

    /**
     * 连接地址
     */
    private String addresses;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 发布者返回
     */
    private Boolean publisherReturns;

    /**
     * 虚拟机
     */
    private String virtualHost;

    /**
     * 缓存模式
     */
    private CachingConnectionFactory.CacheMode cacheMode;

    /**
     * 默认队列名字
     */
    private String defaultQueueName = "gapleaf";
    
    /**
     * canal的queue的名字
     */
    private String canalQueue = "canalQueue";
}
