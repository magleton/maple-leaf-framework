package com.geoxus.core.rpc.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.rpc.service.GXRabbitMQRPCClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXRabbitMQRPCClientServiceImpl implements GXRabbitMQRPCClientService {
    @GXFieldCommentAnnotation(zhDesc = "Direct Reply-To")
    private static final String REPLY_TO = "amq.rabbitmq.reply-to";

    @Autowired
    private RabbitTemplate rpcRabbitTemplate;

    @Override
    public Dict call(Dict param, String rpcQueue) {
        String threadName = Thread.currentThread().getName();
        rpcRabbitTemplate.setReplyErrorHandler(throwable -> {
            final MessageProperties properties = ((ListenerExecutionFailedException) throwable).getFailedMessage().getMessageProperties();
            final String message = throwable.getMessage();
            final String causeMsg = throwable.getCause().getMessage();
            final Object correlationId = Optional.ofNullable(properties.getHeader("correlation_id")).orElse("");
            log.error("RPC调用超时原因: {} . {} , correlationId : {} ,  请求参数 : {} , RPC请求队列 : {} , 线程 : {} ", message, causeMsg, correlationId, param, rpcQueue, threadName);
        });
        final String correlationId = IdUtil.randomUUID();
        final Object rpcResult = rpcRabbitTemplate.convertSendAndReceive(rpcQueue, param, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            message.getMessageProperties().setReplyTo(REPLY_TO);
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            message.getMessageProperties().getHeaders().put("correlation_id", correlationId);
            return message;
        }, new CorrelationData(correlationId));
        if (null != rpcResult) {
            String rpcCallStr;
            if (rpcResult instanceof byte[]) {
                rpcCallStr = new String((byte[]) rpcResult, StandardCharsets.UTF_8);
            } else {
                rpcCallStr = (String) rpcResult;
            }
            Dict data = JSONUtil.toBean(rpcCallStr, Dict.class);
            data.putIfAbsent("code", HttpStatus.HTTP_OK);
            return data;
        }
        return param.set("code", HttpStatus.HTTP_INTERNAL_ERROR).set("msg", "RPC调用失败").set("correlation_id", correlationId).set("thread_name", StrUtil.format("{}", threadName));
    }

    @Override
    public Dict call(String rpcMethodName, String rpcServiceHandler, Dict param, String rpcQueue) {
        param.set("rpc", Dict.create().set("method", rpcMethodName).set("handler", rpcServiceHandler));
        return call(param, rpcQueue);
    }
}
