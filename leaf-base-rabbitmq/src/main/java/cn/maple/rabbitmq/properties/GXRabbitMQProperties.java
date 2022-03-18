package cn.maple.rabbitmq.properties;

import lombok.Data;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

@Data
public class GXRabbitMQProperties {
    /**
     * 默认队列名字
     */
    protected String defaultQueueName = "gapleaf";

    /**
     * 确认模式
     */
    protected CachingConnectionFactory.ConfirmType publisherConfirmType;

    /**
     * 连接地址
     */
    protected String addresses;

    /**
     * 用户名
     */
    protected String username;

    /**
     * 密码
     */
    protected String password;

    /**
     * 发布者返回
     */
    protected Boolean publisherReturns;

    /**
     * 虚拟机
     */
    protected String virtualHost;

    /**
     * 缓存模式
     */
    protected CachingConnectionFactory.CacheMode cacheMode;
}
