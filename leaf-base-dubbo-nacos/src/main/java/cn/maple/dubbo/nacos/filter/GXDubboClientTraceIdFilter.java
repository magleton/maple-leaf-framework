package cn.maple.dubbo.nacos.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

@Activate(group = {CommonConstants.CONSUMER}, order = Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class GXDubboClientTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            // 通过GXPenetrateAttachmentSelector设置的值可以通过以下方式获取
            // RpcContext.getCurrentServiceContext().getObjectAttachment(GXTraceIdContextUtils.TRACE_ID_KEY)
            // 在GXBaseRequestLoggingFilter中会设置该值
            String traceId = GXTraceIdContextUtils.getTraceId();
            if (CharSequenceUtil.isEmpty(traceId)) {
                // 当改服务既是服务方又是消费方时 会在GXDubboServerTraceIdFilter设置该值
                traceId = RpcContext.getClientAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
            }
            log.info("Dubbo消费者生成的TraceId : {}", traceId);
            GXTraceIdContextUtils.setTraceId(traceId);
            RpcContext.getClientAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
            return invoker.invoke(invocation);
        } finally {
            GXTraceIdContextUtils.removeTraceId();
        }
    }
}
