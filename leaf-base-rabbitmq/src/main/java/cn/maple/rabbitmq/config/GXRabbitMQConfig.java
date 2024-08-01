package cn.maple.rabbitmq.config;

import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.rabbitmq.callback.GXConfirmCallback;
import cn.maple.rabbitmq.callback.GXRecoveryCallback;
import cn.maple.rabbitmq.callback.GXReturnsCallback;
import cn.maple.rabbitmq.properties.GXRabbitMQProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.GenericMessageConverter;

@Configuration
@Slf4j
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
@EnableRabbit
public class GXRabbitMQConfig {
    @Resource
    private GXRabbitMQProperties rabbitMQProperties;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setReturnsCallback(returned -> {
            GXReturnsCallback returnsCallback = GXSpringContextUtils.getBean(GXReturnsCallback.class);
            if (ObjectUtil.isNotNull(returnsCallback)) {
                returnsCallback.returnedMessage(returned);
            }
        });
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            GXConfirmCallback confirmCallback = GXSpringContextUtils.getBean(GXConfirmCallback.class);
            if (ObjectUtil.isNotNull(confirmCallback)) {
                confirmCallback.confirm(correlationData, ack, cause);
            }
        });
        rabbitTemplate.setRecoveryCallback(retryContext -> {
            GXRecoveryCallback recoveryCallback = GXSpringContextUtils.getBean(GXRecoveryCallback.class);
            if (ObjectUtil.isNotNull(recoveryCallback)) {
                return recoveryCallback.recover(retryContext);
            }
            return null;
        });
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(@Autowired RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

    /**
     * 初始化 RabbitMessagingTemplate
     *
     * @param connectionFactory 连接工厂
     * @return RabbitMessagingTemplate
     */
    @Bean
    public RabbitMessagingTemplate simpleMessageTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setMessageConverter(new GenericMessageConverter());
        rabbitMessagingTemplate.setRabbitTemplate(template);
        return rabbitMessagingTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setPublisherConfirmType(rabbitMQProperties.getPublisherConfirmType());
        cachingConnectionFactory.setAddresses(GXCommonUtils.decodeConnectStr(rabbitMQProperties.getAddresses(), String.class));
        cachingConnectionFactory.setUsername(GXCommonUtils.decodeConnectStr(rabbitMQProperties.getUsername(), String.class));
        cachingConnectionFactory.setPassword(GXCommonUtils.decodeConnectStr(rabbitMQProperties.getPassword(), String.class));
        cachingConnectionFactory.setPublisherReturns(rabbitMQProperties.getPublisherReturns());
        cachingConnectionFactory.setVirtualHost(GXCommonUtils.decodeConnectStr(rabbitMQProperties.getVirtualHost(), String.class));
        cachingConnectionFactory.setCacheMode(rabbitMQProperties.getCacheMode());
        return cachingConnectionFactory;
    }
}
