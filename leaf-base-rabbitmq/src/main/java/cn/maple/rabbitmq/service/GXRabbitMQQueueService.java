package cn.maple.rabbitmq.service;

import org.springframework.amqp.core.Message;

public interface GXRabbitMQQueueService {
    /**
     * 处理常规队列
     *
     * @param data 消息数据
     */
    void process(Message data);
}
