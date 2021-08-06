package com.geoxus.core.common.service;

import org.springframework.amqp.core.Message;

public interface GXRabbitMQQueueService {
    /**
     * 处理常规队列
     *
     * @param data
     */
    void process(Message data);
}
