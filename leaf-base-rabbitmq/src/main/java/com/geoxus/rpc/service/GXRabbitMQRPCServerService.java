package com.geoxus.rpc.service;

import com.geoxus.core.framework.annotation.GXFieldComment;
import com.geoxus.core.framework.util.GXSpringContextUtil;
import org.springframework.amqp.core.Message;

import java.util.Optional;

public interface GXRabbitMQRPCServerService {
    @GXFieldComment("默认队列的名字")
    String DEFAULT_QUEUE_NAME = "java_rpc_queue";

    /**
     * 处理RPC
     *
     * @param message
     * @return
     */
    String rpcServer(Message message);

    /**
     * 获取队列名字
     *
     * @param queueName
     * @return
     */
    default String getQueueName(String queueName) {
        queueName = Optional.ofNullable(queueName).orElse("rabbit.rpc-server-name");
        return GXSpringContextUtil.getEnvironment().getProperty(queueName, String.class);
    }
}
