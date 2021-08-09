package com.geoxus.listener;

import com.geoxus.service.GXCanalMessageParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class GXCanalRabbitMQListener {
    @Resource
    private GXCanalMessageParseService canalMessageParseService;

    /*    @RabbitListener(bindings = {
                @QueueBinding(
                        value = @Queue(value = CanalConstant.RABBITMQ_CANAL_QUEUE_NAME, durable = "true"),
                        exchange = @Exchange(value = CanalConstant.RABBITMQ_CANAL_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
                        key = CanalConstant.RABBITMQ_CANAL_ROUTING_KEY
                )
        }, concurrency = CanalConstant.RABBITMQ_CANAL_CONCURRENCY_COUNT)*/
    public void listener(String message) {
        canalMessageParseService.parseMessage(message);
    }
}