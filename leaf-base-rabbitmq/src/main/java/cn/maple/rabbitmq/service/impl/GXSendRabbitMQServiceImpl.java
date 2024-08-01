package cn.maple.rabbitmq.service.impl;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.rabbitmq.dto.inner.GXRabbitMQMessageReqDto;
import cn.maple.rabbitmq.service.GXSendRabbitMQService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GXSendRabbitMQServiceImpl extends GXBusinessServiceImpl implements GXSendRabbitMQService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送常规消息的ACT消息
     *
     * @param messageReqDto 待发送的消息
     */
    public void sendNormalMessage(GXRabbitMQMessageReqDto messageReqDto) {
        Dict message = messageReqDto.getMessage();
        String exchange = messageReqDto.getExchange();
        String routingKey = messageReqDto.getRoutingKey();
        CorrelationData correlationData = messageReqDto.getCorrelationData();
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
    }
}