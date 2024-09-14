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
 * 服务作为dubbo客户端时会被调用
 * 调用顺序为
 * 先调用{@link GXPenetrateAttachmentSelector#select(Invocation invocation, RpcContextAttachment clientAttachment, RpcContextAttachment serverAttachment)}
 * 然后才会调用{@link GXDubboClientTraceIdFilter#invoke(Invoker<?> invoker, Invocation invocation)}方法
 */
@Activate(group = {CommonConstants.CONSUMER}, order = Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class GXDubboClientTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 通过GXPenetrateAttachmentSelector设置的值可以通过以下方式获取
        // RpcContext.getCurrentServiceContext().getObjectAttachment(GXTraceIdContextUtils.TRACE_ID_KEY)
        // 在GXBaseRequestLoggingFilter中会设置该值
        String traceId = GXTraceIdContextUtils.getTraceId();
        if (CharSequenceUtil.isEmpty(traceId)) {
            // 当改服务既是服务方又是消费方时 会在GXDubboServerTraceIdFilter设置该值
            traceId = RpcContext.getClientAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
            if (CharSequenceUtil.isEmpty(traceId)) {
                traceId = RpcContext.getServerAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
            }
        }
        String appName = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class);
        log.info("【{} --->> Dubbo Client】生成TraceId : {}", appName, traceId);
        GXTraceIdContextUtils.setTraceId(traceId);
        invocation.setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
        RpcContext.getServerAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
        RpcContext.getClientAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
        return invoker.invoke(invocation);
    }
}
