package cn.maple.rabbitmq.util;

import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.rabbitmq.rpc.config.GXRabbitMQRPCRemoteServersConfig;
import cn.maple.rabbitmq.rpc.service.GXRabbitMQRPCClientService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.converter.GenericMessageConverter;

import java.util.Map;

/**
 * RabbitMQ 公共工具类
 */
public class GXRabbitUtils {
    private GXRabbitUtils() {

    }

    /**
     * 初始化 ConnectionFactory
     *
     * @param addresses 地址列表
     * @param username  用户名
     * @param password  密码
     * @param vHost     虚拟机
     * @return ConnectionFactory
     * @throws Exception
     */
    public static ConnectionFactory connectionFactory(String addresses, String username, String password, String vHost) throws Exception {
        CachingConnectionFactory factoryBean = new CachingConnectionFactory();
        factoryBean.setVirtualHost(vHost);
        factoryBean.setAddresses(addresses);
        factoryBean.setUsername(username);
        factoryBean.setPassword(password);
        return factoryBean;
    }

    /**
     * 初始化 RabbitMessagingTemplate
     *
     * @param connectionFactory 连接工厂
     * @return RabbitMessagingTemplate
     */
    public static RabbitMessagingTemplate simpleMessageTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setMessageConverter(new GenericMessageConverter());
        rabbitMessagingTemplate.setRabbitTemplate(template);
        return rabbitMessagingTemplate;
    }

    /**
     * 获取RPC远程服务的名字
     *
     * @param serverName 服务器名字
     * @param key        key
     * @return String
     */
    public static String getRemoteRPCServerValueByKey(String serverName, String key) {
        final GXRabbitMQRPCRemoteServersConfig serverConfigBean = GXSpringContextUtils.getBean(GXRabbitMQRPCRemoteServersConfig.class);
        assert serverConfigBean != null;
        final Map<String, Map<String, Object>> servers = serverConfigBean.getServers();
        final Map<String, Object> server = servers.get(serverName);
        if (null == server) {
            GXRabbitMQRPCClientService.LOG.error("{} 远程RPC服务不存在", serverName);
            return "";
        }
        if (null != server.get(key)) {
            return (String) server.get(key);
        }
        return "";
    }
}