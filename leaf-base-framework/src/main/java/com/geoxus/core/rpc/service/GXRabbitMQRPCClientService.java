package com.geoxus.core.rpc.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface GXRabbitMQRPCClientService {
    @GXFieldCommentAnnotation("日志对象")
    Logger LOG = LoggerFactory.getLogger(GXRabbitMQRPCClientService.class);

    @GXFieldCommentAnnotation("PHP的RPC队列名字")
    String PHP_RPC_QUEUE = "php_rpc_queue";

    @GXFieldCommentAnnotation("JAVA的RPC队列名字")
    String JAVA_RPC_QUEUE = "java_rpc_queue";

    Dict call(Dict param, String rpcQueue);

    Dict call(String rpcMethodName, String rpcServiceHandler, Dict param, String rpcQueue);
}
