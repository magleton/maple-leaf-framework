package com.geoxus.rabbitmq.rpc.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.framework.annotation.GXFieldComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.DirectRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXRabbitMQRPCServerConfig {
    @GXFieldComment(zhDesc = "当前系统的处理器个数")
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    @GXFieldComment(zhDesc = "线程池中线程的个数")
    private static final int THREAD_POOL_NUMBER = AVAILABLE_PROCESSORS * 5;

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    @Resource
    private ConnectionFactory connectionFactory;

    @Bean(value = "rpcRabbitTemplate")
    public RabbitTemplate rpcRabbitTemplate() {
        final RabbitTemplate rpcRabbitTemplate = new RabbitTemplate();
        rpcRabbitTemplate.setConnectionFactory(connectionFactory);
        rpcRabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rpcRabbitTemplate;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_NUMBER);
        container.setTaskExecutor(executorService);
        container.setConcurrentConsumers(THREAD_POOL_NUMBER);
        container.setPrefetchCount(THREAD_POOL_NUMBER);
        container.setExposeListenerChannel(true);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            final long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliveryTag, true);
            // 可以在这里处理消息的手动回复等操作......
        });
        return container;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_NUMBER);
        factory.setTaskExecutor(executorService);
        factory.setConcurrentConsumers(AVAILABLE_PROCESSORS);
        factory.setMaxConcurrentConsumers(THREAD_POOL_NUMBER);
        factory.setErrorHandler(throwable -> log.error(CharSequenceUtil.format("simpleRabbitListenerContainerFactory : {}", throwable.getMessage()), throwable));
        factory.setPrefetchCount(AVAILABLE_PROCESSORS);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setBeforeSendReplyPostProcessors(message -> {
            final MessageProperties properties = message.getMessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        });
        factory.setAfterReceivePostProcessors(message -> {
            final MessageProperties properties = message.getMessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        });
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public DirectRabbitListenerContainerFactory directRabbitListenerContainerFactory(DirectRabbitListenerContainerFactoryConfigurer configurer) {
        DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_NUMBER);
        factory.setTaskExecutor(executorService);
        factory.setConsumersPerQueue(AVAILABLE_PROCESSORS);
        factory.setErrorHandler(throwable -> log.error(CharSequenceUtil.format("directRabbitListenerContainerFactory : {}", throwable.getMessage()), throwable));
        factory.setPrefetchCount(AVAILABLE_PROCESSORS);
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setDefaultRequeueRejected(false);
        factory.setAfterReceivePostProcessors(message -> {
            final MessageProperties properties = message.getMessageProperties();
            THREAD_LOCAL.set(properties.getHeader("correlation_id"));
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        });
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setBeforeSendReplyPostProcessors(message -> {
            final MessageProperties properties = message.getMessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            final String correlationId = THREAD_LOCAL.get();
            properties.setHeader("correlation_id", correlationId);
            THREAD_LOCAL.remove();
            return message;
        });
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
