package cn.maple.dubbo.nacos.filter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

import java.util.Optional;

@Activate(group = {CommonConstants.CONSUMER}, order = Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class GXDubboClientTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = Optional.ofNullable(invocation.getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY)).orElse(GXTraceIdContextUtils.getTraceId());
        if (CharSequenceUtil.isEmpty(traceId)) {
            // 理论上TraceId只能是由客户端生成,如果出现这种TraceId,则需要排查
            traceId = CharSequenceUtil.format("{}-{}", "TraceId", DateUtil.format(DateUtil.date(), DatePattern.CHINESE_DATE_TIME_PATTERN));
        }
        log.debug("Dubbo消费者生成的TraceId : {}", traceId);
        GXTraceIdContextUtils.setTraceId(traceId);
        RpcContext.getServerAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
        return invoker.invoke(invocation);
    }
}
