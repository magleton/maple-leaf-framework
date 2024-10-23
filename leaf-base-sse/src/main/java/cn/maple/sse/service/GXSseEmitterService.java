package cn.maple.sse.service;

import cn.maple.core.framework.service.GXBusinessService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 基于Spring Boot实现的Server-sent events
 */
public interface GXSseEmitterService extends GXBusinessService {
    /**
     * 创建连接
     *
     * @param clientId 客户端ID
     * @param timeout  超时时间
     */
    SseEmitter createSseConnect(String clientId, long timeout);

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
     * @param splitMsg 是否需要拆分消息
     */
    void sendMessageToOneClient(String clientId, String msg, boolean splitMsg);

    /**
     * 关闭连接
     *
     * @param clientId 客户端ID
     */
    void closeConnect(String clientId);

    /**
     * 消息拆分
     * 将长的消息拆分成小的消息
     *
     * @param msg    待拆分的消息
     * @param length 随机的字符长度
     * @return 拆分之后的消息
     */
    List<String> splitMessage(String msg, int length);
}
