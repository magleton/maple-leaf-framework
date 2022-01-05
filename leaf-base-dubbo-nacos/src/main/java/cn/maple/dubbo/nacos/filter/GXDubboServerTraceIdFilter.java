package cn.maple.dubbo.nacos.filter;

import cn.maple.core.framework.util.GXTraceIdContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

import java.util.Optional;

@Activate(group = {CommonConstants.PROVIDER}, order = Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class GXDubboServerTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = Optional.ofNullable(invocation.getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY)).orElse(GXTraceIdContextUtils.getTraceId());
        log.debug("Dubbo服务提供者获取到的TraceId : {}", traceId);
        GXTraceIdContextUtils.setTraceId(traceId);
        RpcContext.getClientAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
        return invoker.invoke(invocation);
    }
}
