package cn.maple.rabbitmq.rpc.service;

import cn.hutool.core.lang.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface GXRabbitMQRPCClientService {
    /**
     * 日志对象
     */
    Logger LOG = LoggerFactory.getLogger(GXRabbitMQRPCClientService.class);

    /**
     * PHP的RPC队列名字
     */
    String PHP_RPC_QUEUE = "php_rpc_queue";

    /**
     * JAVA的RPC队列名字
     */
    String JAVA_RPC_QUEUE = "java_rpc_queue";

    Dict call(Dict param, String rpcQueue);

    Dict call(String rpcMethodName, String rpcServiceHandler, Dict param, String rpcQueue);
}
