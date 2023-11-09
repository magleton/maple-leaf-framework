package cn.maple.dubbo.nacos.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import cn.maple.dubbo.nacos.selector.GXPenetrateAttachmentSelector;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

/**
 * 服务作为dubbo服务方 该方法会被调用
 * 先调用{@link GXDubboServerTraceIdFilter#invoke(Invoker<?> invoker, Invocation invocation)}方法
 * 然后才会调用{@link GXPenetrateAttachmentSelector#electReverse(Invocation invocation, RpcContextAttachment clientResponseContext, RpcContextAttachment serverResponseContext)}方法
 */
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
                if (CharSequenceUtil.isEmpty(traceId)) {
                    traceId = RpcContext.getClientAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
                }
            }
            String appName = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class);
            log.info("【{} --->> Dubbo Service】获取TraceId : {}", appName, traceId);
            GXTraceIdContextUtils.setTraceId(traceId);
            invocation.setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
            RpcContext.getClientAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
            RpcContext.getServerAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
            return invoker.invoke(invocation);
        } finally {
            GXTraceIdContextUtils.removeTraceId();
        }
    }
}
