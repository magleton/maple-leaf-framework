package cn.maple.rabbitmq.service;

import cn.maple.core.framework.service.GXBusinessService;
import cn.maple.rabbitmq.dto.inner.GXRabbitMQMessageReqDto;

public interface GXSendRabbitMQService extends GXBusinessService {
    /**
     * 发送常规消息的ACT消息
     *
     * @param messageReqDto 待发送的消息
     */
    void sendNormalMessage(GXRabbitMQMessageReqDto messageReqDto);
}