package cn.maple.rocketmq.service;


import cn.maple.core.framework.service.GXBusinessService;
import cn.maple.rocketmq.dto.inner.GXRocketMQMessageReqDto;

public interface GXSendMQService extends GXBusinessService {
    /**
     * 发送常规消息的ACT消息
     *
     * @param messageReqDto 待发送的消息
     */
    void sendNormalMessage(GXRocketMQMessageReqDto messageReqDto);

    /**
     * 发送延迟消息
     *
     * @param messageReqDto 待发送的消息
     */
    String sendDelayMessage(GXRocketMQMessageReqDto messageReqDto);

    /**
     * 异步发送
     *
     * @param messageReqDto 待发送的消息
     */
    boolean sendAsync(GXRocketMQMessageReqDto messageReqDto);

    /**
     * 快速发送
     *
     * @param messageReqDto 待发送的消息对象
     */
    boolean sendOneway(GXRocketMQMessageReqDto messageReqDto);

    /**
     * 同步消息
     *
     * @param messageReqDto 待发送的消息对象
     */
    boolean send(GXRocketMQMessageReqDto messageReqDto);
}