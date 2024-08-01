package cn.maple.rabbitmq.listener;

import org.springframework.amqp.core.Message;

public interface GXRabbitMQQueueListener {
    /**
     * 处理常规队列
     *
     * @param data 消息数据
     */
    void process(Message data);
}
