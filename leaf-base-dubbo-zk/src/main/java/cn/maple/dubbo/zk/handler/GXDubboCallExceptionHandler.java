package cn.maple.dubbo.zk.handler;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.exception.GXExceptionHandler;
import cn.maple.core.framework.util.GXResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GXDubboCallExceptionHandler extends GXExceptionHandler {
    @ExceptionHandler(RpcException.class)
    public GXResultUtils<Dict> handleRpcException(RpcException e) {
        log.error(e.getMessage(), e);
        return GXResultUtils.ok("服务端没有正常相应,请检查!");
    }
}
