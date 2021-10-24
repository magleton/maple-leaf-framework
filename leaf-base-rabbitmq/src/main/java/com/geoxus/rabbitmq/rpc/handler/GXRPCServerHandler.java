package com.geoxus.rabbitmq.rpc.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.datasource.service.GXDBBaseService;

import java.lang.reflect.Method;
import java.util.Optional;

public interface GXRPCServerHandler {
    /**
     * 默认的RPC处理器
     *
     * @param param 参数
     * @return JSONObject
     */
    default JSONObject rpcHandler(Dict param) {
        final Dict rpcInfo = getRPCCallInfo(param);
        final String methodName = rpcInfo.getStr("method");
        final Method method = ReflectUtil.getMethodByName(getService().getClass(), methodName);
        if (null != method) {
            final Object invoke = ReflectUtil.invoke(getService(), method, param);
            final JSONObject json = JSONUtil.parseObj(invoke);
            json.putByPath("code", HttpStatus.HTTP_OK);
            return json;
        }
        final Dict dict = Dict.create().set("code", HttpStatus.HTTP_INTERNAL_ERROR).set("msg", StrUtil.format("{}.{}服务不存在", rpcInfo.getStr("handler"), methodName));
        return JSONUtil.parseObj(dict);
    }

    /**
     * 获取请求RPC的信息
     *
     * @param param 参数
     * @return Dict
     */
    default Dict getRPCCallInfo(Dict param) {
        return Optional.ofNullable(Convert.convert(Dict.class, JSONUtil.parse(param.getStr("rpc"))))
                .orElse(Dict.create().set("method", "").set("handler", ""));
    }

    /**
     * 获取子类的具体RPC服务
     *
     * @return
     */
    default GXDBBaseService getService() {
        return null;
    }
}
