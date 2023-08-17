package cn.maple.rocketmq.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.rocketmq.dto.inner.GXRocketMQMessageReqDto;
import cn.maple.rocketmq.service.GXSendRocketMQService;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Log4j2
@Service
public class GXSendRocketMQServiceImpl extends GXBusinessServiceImpl implements GXSendRocketMQService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 普通消息
     *
     * @param messageReqDto 待发送的消息
     */
    @Override
    public void sendNormalMessage(GXRocketMQMessageReqDto messageReqDto) {
        log.info("发送普通活动消息开始");
        String messageKey = messageReqDto.getMessageKey();
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(messageReqDto.getBody());
        if (CharSequenceUtil.isNotEmpty(messageKey)) {
            messageBuilder.setHeader(RocketMQHeaders.KEYS, messageKey);
        }
        Message<String> message = messageBuilder.build();
        rocketMQTemplate.send(messageReqDto.getDestination(), message);
        log.info("普通消息 msg = {}", message);
    }

    /**
     * 发送延迟消息
     *
     * @param messageReqDto 待发送的消息
     */
    @Override
    public String sendDelayMessage(GXRocketMQMessageReqDto messageReqDto) {
        log.info("发送延时消息开始");
        //处理消息到时的绝对时间
        long lastTimeOut = System.currentTimeMillis() + messageReqDto.getDeliverTime() * 1000L;
        String messageKey = messageReqDto.getMessageKey();
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(messageReqDto.getBody());
        if (CharSequenceUtil.isNotEmpty(messageKey)) {
            messageBuilder.setHeader(RocketMQHeaders.KEYS, messageKey);
        }
        Message<String> message = messageBuilder.build();
        SendResult sendResult = rocketMQTemplate.syncSendDeliverTimeMills(messageReqDto.getDestination(), message, lastTimeOut);
        log.info("延迟消息发送完毕");
        return sendResult.getMsgId();
    }

    /**
     * 同步消息
     *
     * @param messageReqDto 待发送的消息对象
     */
    public boolean syncSend(GXRocketMQMessageReqDto messageReqDto) {
        //执行发送
        String destination = CharSequenceUtil.format("{}:{}", messageReqDto.toString(), messageReqDto.getTag());
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(messageReqDto.getBody());
        String messageKey = messageReqDto.getMessageKey();
        if (CharSequenceUtil.isNotEmpty(messageKey)) {
            messageBuilder.setHeader(RocketMQHeaders.KEYS, messageKey);
        }
        Message<String> message = messageBuilder.build();
        rocketMQTemplate.syncSend(destination, message);
        log.info("列发送消息成功 ");
        return true;
    }

    /**
     * 异步发送
     *
     * @param messageReqDto 待发送的消息
     */
    public boolean sendAsync(GXRocketMQMessageReqDto messageReqDto) {
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(messageReqDto.getBody());
        String messageKey = messageReqDto.getMessageKey();
        if (CharSequenceUtil.isNotEmpty(messageKey)) {
            messageBuilder.setHeader(RocketMQHeaders.KEYS, messageKey);
        }
        Message<String> message = messageBuilder.build();
        rocketMQTemplate.asyncSend(messageReqDto.getDestination(), message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送信息成功" + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("发送信息失败" + throwable);
            }
        });
        log.info("消息队列发送消息成功");
        return true;
    }

    /**
     * 快速发送
     *
     * @param messageReqDto 待发送的消息对象
     */
    public boolean sendOneway(GXRocketMQMessageReqDto messageReqDto) {
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(messageReqDto.getBody());
        String messageKey = messageReqDto.getMessageKey();
        if (CharSequenceUtil.isNotEmpty(messageKey)) {
            messageBuilder.setHeader(RocketMQHeaders.KEYS, messageKey);
        }
        Message<String> message = messageBuilder.build();
        rocketMQTemplate.sendOneWay(messageReqDto.getDestination(), message);
        log.info("列发送消息成功");
        return true;
    }
}