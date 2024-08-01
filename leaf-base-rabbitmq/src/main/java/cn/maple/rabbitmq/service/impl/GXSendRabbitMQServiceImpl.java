package cn.maple.rabbitmq.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.rabbitmq.dto.inner.GXRabbitMQMessageReqDto;
import cn.maple.rabbitmq.service.GXSendRabbitMQService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

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
        Dict data = messageReqDto.getData();
        String exchange = messageReqDto.getExchange();
        String routingKey = messageReqDto.getRoutingKey();
        CorrelationData correlationData = messageReqDto.getCorrelationData();
        MessageProperties messageProperties = messageReqDto.getMessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
        if (CharSequenceUtil.isNotBlank(messageReqDto.getTag())) {
            messageProperties.setConsumerTag(messageReqDto.getTag());
        }
        Message message = new Message(JSONUtil.toJsonStr(data).getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
    }
}