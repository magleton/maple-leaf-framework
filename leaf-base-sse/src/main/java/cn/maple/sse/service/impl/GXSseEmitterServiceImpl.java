package cn.maple.sse.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.sse.dto.GXMessageDto;
import cn.maple.sse.service.GXSseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@Service
public class GXSseEmitterServiceImpl extends GXBusinessServiceImpl implements GXSseEmitterService {
    /**
     * 容器，保存连接，用于输出返回 ;可使用其他方法实现
     */
    private static final Map<String, SseEmitter> SSE_CLIENT_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据客户端id获取SseEmitter对象
     *
     * @param clientId 客户端ID
     */
    @Override
    public SseEmitter getSseEmitterByClientId(String clientId) {
        return SSE_CLIENT_CACHE.get(clientId);
    }

    /**
     * 创建连接
     *
     * @param clientId 客户端ID
     */
    @Override
    public SseEmitter createConnect(String clientId) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 是否需要给客户端推送ID
        if (CharSequenceUtil.isBlank(clientId)) {
            clientId = IdUtil.simpleUUID();
        }
        // 注册回调
        // 1. 长链接完成后回调接口(即关闭连接时调用)
        sseEmitter.onCompletion(completionCallBack(clientId));
        // 2. 连接超时回调
        sseEmitter.onTimeout(timeoutCallBack(clientId));
        // 3. 推送消息异常时，回调方法
        sseEmitter.onError(errorCallBack(clientId));
        SSE_CLIENT_CACHE.put(clientId, sseEmitter);
        log.info("创建新的sse连接，当前用户：{}    累计用户:{}", clientId, SSE_CLIENT_CACHE.size());
        // 注册成功返回用户信息
        SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event().id(String.valueOf(HttpStatus.HTTP_CREATED)).data(clientId, MediaType.APPLICATION_JSON);
        send(clientId, sseEmitter, sseEventBuilder);
        return sseEmitter;
    }

    /**
     * 发送消息给所有客户端
     *
     * @param msg 消息内容
     */
    @Override
    public void sendMessageToAllClient(String msg) {
        if (MapUtil.isEmpty(SSE_CLIENT_CACHE)) {
            return;
        }
        // 判断发送的消息是否为空
        for (Map.Entry<String, SseEmitter> entry : SSE_CLIENT_CACHE.entrySet()) {
            GXMessageDto messageDto = new GXMessageDto();
            messageDto.setClientId(entry.getKey());
            messageDto.setMessage(msg);
            sendMsgToClientByClientId(entry.getKey(), messageDto, entry.getValue());
        }
    }

    /**
     * 给指定客户端发送消息
     *
     * @param clientId 客户端ID
     * @param msg      消息内容
     */
    @Override
    public void sendMessageToOneClient(String clientId, String msg) {
        GXMessageDto messageVo = new GXMessageDto(clientId, msg);
        sendMsgToClientByClientId(clientId, messageVo, SSE_CLIENT_CACHE.get(clientId));
    }

    /**
     * 关闭连接
     *
     * @param clientId 客户端ID
     */
    @Override
    public void closeConnect(String clientId) {
        SseEmitter sseEmitter = SSE_CLIENT_CACHE.get(clientId);
        if (sseEmitter != null) {
            sseEmitter.complete();
            removeUser(clientId);
        }
    }

    /**
     * 推送消息到客户端
     * 此处做了推送失败后，重试推送机制，可根据自己业务进行修改
     *
     * @param clientId   客户端ID
     * @param messageDto 推送信息，此处结合具体业务，定义自己的返回值即可
     **/
    private void sendMsgToClientByClientId(String clientId, GXMessageDto messageDto, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            log.error("推送消息失败：客户端{}未创建长链接,失败消息:{}", clientId, messageDto.toString());
            return;
        }
        SseEmitter.SseEventBuilder sendData = SseEmitter.event().id(String.valueOf(HttpStatus.HTTP_OK)).data(messageDto, MediaType.APPLICATION_JSON);
        send(clientId, sseEmitter, sendData);
    }

    /**
     * 长链接完成后回调接口(即关闭连接时调用)
     *
     * @param clientId 客户端ID
     **/
    private Runnable completionCallBack(String clientId) {
        return () -> {
            log.info("结束连接：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 连接超时时调用
     *
     * @param clientId 客户端ID
     **/
    private Runnable timeoutCallBack(String clientId) {
        return () -> {
            log.info("连接超时：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 推送消息异常时，回调方法
     *
     * @param clientId 客户端ID
     **/
    private Consumer<Throwable> errorCallBack(String clientId) {
        return throwable -> {
            log.error("GXSseEmitterServiceImpl[errorCallBack]：连接异常,客户端ID:{}", clientId);
            SseEmitter sseEmitter = SSE_CLIENT_CACHE.get(clientId);
            if (sseEmitter == null) {
                log.error("客户端{}不存在长连接。", clientId);
                return;
            }
            GXMessageDto messageDto = new GXMessageDto(clientId, "失败后重新推送");
            SseEmitter.SseEventBuilder sendData = SseEmitter.event().id(String.valueOf(HttpStatus.HTTP_OK)).data(messageDto, MediaType.APPLICATION_JSON);
            send(clientId, sseEmitter, sendData);
        };
    }

    /**
     * 移除用户连接
     *
     * @param clientId 客户端ID
     **/
    private void removeUser(String clientId) {
        SSE_CLIENT_CACHE.remove(clientId);
        log.info("SseEmitterServiceImpl[removeUser]:移除用户：{}", clientId);
    }

    /**
     * Spring retry框架
     *
     * @param clientId        客户端ID
     * @param sseEmitter      sseEmitter对象
     * @param sseEventBuilder 事件参数构造构造器
     */
    @Override
    @Retryable(value = IOException.class, maxAttempts = 5, backoff = @Backoff(delay = 10000))
    public void send(String clientId, SseEmitter sseEmitter, SseEmitter.SseEventBuilder sseEventBuilder) {
        try {
            sseEmitter.send(sseEventBuilder);
        } catch (IOException e) {
            log.error("SSE客户端链接异常，客户端ID:{},异常信息:{}", clientId, e.getMessage());
        }
    }

    /**
     * 恢复逻辑
     *
     * @param ex              异常信息
     * @param clientId        客户端ID
     * @param sseEventBuilder 消息builder
     */
    @Override
    @Recover
    public void recover(Exception ex, String clientId, SseEmitter.SseEventBuilder sseEventBuilder) {
        log.error("重试5次向{}发送消息失败。消息:{}", clientId, sseEventBuilder);
    }
}
