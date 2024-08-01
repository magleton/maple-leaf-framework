package cn.maple.rabbitmq.callback;

import org.springframework.amqp.core.ReturnedMessage;

public interface GXReturnsCallback {
    void returnedMessage(ReturnedMessage returned);
}
