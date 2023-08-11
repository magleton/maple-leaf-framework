package cn.maple.sse.service;

import cn.maple.core.framework.service.GXBusinessService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 基于Spring Boot实现的Server-sent events
 */
public interface GXSseEmitterService extends GXBusinessService {
    /**
     * 创建连接
     *
     * @param clientId 客户端ID
     */
    SseEmitter createConnect(String clientId);

    /**
     * 根据客户端id获取SseEmitter对象
     *
     * @param clientId 客户端ID
     */
    SseEmitter getSseEmitterByClientId(String clientId);

    /**
     * 发送消息给所有客户端
     *
     * @param msg 消息内容
     */
    void sendMessageToAllClient(String msg);

    /**
     * 给指定客户端发送消息
     *
     * @param clientId 客户端ID
     * @param msg      消息内容
     */
    void sendMessageToOneClient(String clientId, String msg);

    /**
     * 关闭连接
     *
     * @param clientId 客户端ID
     */
    void closeConnect(String clientId);

    /**
     * 统一发送消息
     *
     * @param clientId        客户端ID
     * @param sseEmitter      sseEmitter对象
     * @param sseEventBuilder 事件参数构造构造器
     */
    void send(String clientId, SseEmitter sseEmitter, SseEmitter.SseEventBuilder sseEventBuilder);
}
