package com.geoxus.core.rpc.handler.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.rpc.handler.GXRPCServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Slf4j
@Component("defaultRPCServerHandler")
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXDefaultRPCServerHandlerImpl implements GXRPCServerHandler {
    @Override
    public JSONObject rpcHandler(Dict param) {
        log.info("dict param : " + param.toString());
        param.set("result", "处理的结果是 : ABC");
        return JSONUtil.parseObj(param);
    }
}
