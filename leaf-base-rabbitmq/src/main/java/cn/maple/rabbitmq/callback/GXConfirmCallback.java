package cn.maple.rabbitmq.callback;

import org.springframework.amqp.rabbit.connection.CorrelationData;

public interface GXConfirmCallback {
    void confirm(CorrelationData correlationData, boolean ack, String cause);
}
