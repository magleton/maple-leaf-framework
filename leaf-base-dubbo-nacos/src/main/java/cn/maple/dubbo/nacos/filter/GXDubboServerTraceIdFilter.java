package cn.maple.dubbo.nacos.filter;

import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

import java.util.Optional;

@Activate(group = {CommonConstants.PROVIDER}, order = Ordered.LOWEST_PRECEDENCE)
public class GXDubboServerTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceIdAttachment = Optional.ofNullable(invocation.getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY)).orElse(GXTraceIdContextUtils.getTraceId());
        GXTraceIdContextUtils.setTraceId(traceIdAttachment);
        RpcContext.getServerAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceIdAttachment);
        return invoker.invoke(invocation);
    }
}
