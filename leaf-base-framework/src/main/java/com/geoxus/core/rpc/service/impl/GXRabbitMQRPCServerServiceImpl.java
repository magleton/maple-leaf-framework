package com.geoxus.core.rpc.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.config.GXRabbitMQConfig;
import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.rpc.handler.GXRPCServerHandler;
import com.geoxus.core.rpc.service.GXRabbitMQRPCServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@PropertySource(value = "classpath:/ymls/${spring.profiles.active}/rabbit.yml",
        factory = GXYamlPropertySourceFactory.class,
        ignoreResourceNotFound = true)
@ConditionalOnExpression("'${enable-rabbitmq-rpc}'.equals('true')")
@ConditionalOnClass(value = {GXRabbitMQConfig.class},
        name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXRabbitMQRPCServerServiceImpl implements GXRabbitMQRPCServerService {
    @Override
    @RabbitListener(queues = {"${rabbit.rpc-server.local.default-server-name}"}, containerFactory = "directRabbitListenerContainerFactory")
    @RabbitHandler
    public String rpcServer(Message message) {
        String threadName = Thread.currentThread().getName();
        final String s = new String(message.getBody(), StandardCharsets.UTF_8);
        if (JSONUtil.isJson(s) || JSONUtil.isJsonArray(s)) {
            final String data = handleData(s);
            return JSONUtil.toJsonStr(data);
        }
        final Dict errorData = Dict.create().set("thread_name", threadName).set("msg", StrUtil.format("参数-{}: 不是有效的JSON数据", message)).set("code", HttpStatus.HTTP_INTERNAL_ERROR);
        return JSONUtil.toJsonStr(errorData);
    }

    private String handleData(String data) {
        String threadName = Thread.currentThread().getName();
        final Dict dict = JSONUtil.toBean(data, Dict.class);
        final JSON rpc = JSONUtil.parse(dict.getStr("rpc"));
        if (rpc == null) {
            return JSONUtil.toJsonStr(Dict.create().set("thread_name", threadName).set("msg", "请求参数没有包含RPC调用信息").set("code", HttpStatus.HTTP_INTERNAL_ERROR));
        }
        final String handlerName = rpc.getByPath("handler", String.class);
        if (null == handlerName) {
            return JSONUtil.toJsonStr(Dict.create().set("thread_name", threadName).set("msg", "RPC的处理服务没有设置").set("code", HttpStatus.HTTP_INTERNAL_ERROR));
        }
        final String methodName = rpc.getByPath("method", String.class);
        if (null == methodName) {
            return JSONUtil.toJsonStr(Dict.create().set("thread_name", threadName).set("msg", "RPC的调用方法没有设置").set("code", HttpStatus.HTTP_INTERNAL_ERROR));
        }
        final GXRPCServerHandler rpcServerHandler = GXSpringContextUtils.getBean(handlerName, GXRPCServerHandler.class);
        if (rpcServerHandler == null) {
            return JSONUtil.toJsonStr(Dict.create().set("thread_name", threadName).set("msg", "服务端没有提供对应的RPC服务").set("code", HttpStatus.HTTP_INTERNAL_ERROR));
        }
        try {
            Object o = rpcServerHandler.rpcHandler(dict);
            if (null != o) {
                return JSONUtil.toJsonStr(o);
            }
            return JSONUtil.toJsonStr(Dict.create().set("thread_name", threadName).set("default", "RPC Server Default Value"));
        } catch (Exception e) {
            log.error(StrUtil.format("当前线程名字 {} , 异常消息 : {} , 请求服务的名字 : {} , 请求服务的方法名字 : {} , 请求参数 : {} , 异常信息详细信息 : ",
                    threadName, e.getMessage(), handlerName, methodName, data), e);
            Throwable cause = e.getCause();
            StackTraceElement[] firstCausedBy = null;
            StackTraceElement[] secondCausedBy = null;
            if (null != cause) {
                firstCausedBy = cause.getStackTrace();
            }
            if (null != cause && null != cause.getCause()) {
                secondCausedBy = cause.getCause().getStackTrace();
            }
            return JSONUtil.toJsonStr(Dict.create()
                    .set("gx_call_info", StrUtil.format("RPC调用出错: 调用信息[HandlerName : {} , MethodName : {}]  , 调用参数: {}", handlerName, methodName, JSONUtil.parseObj(data)))
                    .set("gx_error_info", e)
                    .set("gx_first_caused_by", Optional.ofNullable(firstCausedBy).orElse(new StackTraceElement[]{}))
                    .set("gx_second_caused_by", Optional.ofNullable(secondCausedBy).orElse(new StackTraceElement[]{}))
                    .set("gx_code", HttpStatus.HTTP_INTERNAL_ERROR)
            );
        }
    }
}
