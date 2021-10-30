package cn.maple.canal.service;

import cn.hutool.core.lang.Dict;

public interface GXCanalMessageParseService {
    /**
     * 解析RabbitMQ中canal的消息信息
     *
     * @param message 消息
     * @return Dict
     */
    Dict parseMessage(String message);
}
