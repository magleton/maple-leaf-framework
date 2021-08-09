package com.geoxus.listener;

import com.geoxus.constant.CanalConstant;
import com.geoxus.service.GXCanalMessageParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class GXCanalRabbitMQListener {
    @Resource
    private GXCanalMessageParseService canalMessageParseService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "${canal.queue}", durable = "true"),
                    exchange = @Exchange(value = CanalConstant.RABBITMQ_CANAL_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
                    key = CanalConstant.RABBITMQ_CANAL_ROUTING_KEY
            )
    }, concurrency = CanalConstant.RABBITMQ_CANAL_CONCURRENCY_COUNT)
    public void listener(String message) {
        canalMessageParseService.parseMessage(message);
    }
}