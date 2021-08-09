package com.geoxus.listener;

import cn.hutool.json.JSONUtil;
import com.geoxus.dto.GXCanalDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GXCanalRabbitMQListener {
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "example", durable = "true"),
                    exchange = @Exchange(value = "exchange.trade.order-ex", type = ExchangeTypes.DIRECT),
                    key = "exchange.trade.order"
            )
    }, concurrency = "10")
    public void test(String message) {
        final GXCanalDataDto GXCanalDataDto = JSONUtil.toBean(message, GXCanalDataDto.class);
        log.info("test接收到消息。message:{}", message);
    }
}