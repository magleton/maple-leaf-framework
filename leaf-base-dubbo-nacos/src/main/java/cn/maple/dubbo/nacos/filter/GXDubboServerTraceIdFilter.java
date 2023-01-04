package cn.maple.dubbo.nacos.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

@Activate(group = {CommonConstants.PROVIDER}, order = Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class GXDubboServerTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            // 通过GXPenetrateAttachmentSelector设置的值可以通过以下方式获取
            // RpcContext.getCurrentServiceContext().getObjectAttachment(GXTraceIdContextUtils.TRACE_ID_KEY)
            String traceId = GXTraceIdContextUtils.getTraceId();
            if (CharSequenceUtil.isEmpty(traceId)) {
                traceId = RpcContext.getServerAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
            }
            log.info("Dubbo服务提供者获取到的TraceId : {}", traceId);
            GXTraceIdContextUtils.setTraceId(traceId);
            return invoker.invoke(invocation);
        } finally {
            GXTraceIdContextUtils.removeTraceId();
        }
    }
}
