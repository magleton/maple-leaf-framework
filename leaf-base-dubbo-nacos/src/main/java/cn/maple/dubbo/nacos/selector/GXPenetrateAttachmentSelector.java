package cn.maple.dubbo.nacos.selector;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import cn.maple.dubbo.nacos.filter.GXDubboClientTraceIdFilter;
import cn.maple.dubbo.nacos.filter.GXDubboServerTraceIdFilter;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.PenetrateAttachmentSelector;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class GXPenetrateAttachmentSelector implements PenetrateAttachmentSelector {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(PenetrateAttachmentSelector.class);

    /**
     * Select some attachments to pass to next hop.
     * These attachments can fetch from {@link RpcContext#getServerAttachment()} or user defined.
     * <p>
     * 该方法会在该服务作为客户端时进行调用
     * <p>
     * 高方法被调用之后会调用{@link GXDubboClientTraceIdFilter#invoke(Invoker<?> invoker, Invocation invocation)}方法
     * 该方法会将数据写入Invocation对象
     * 相当于调用Invocation.addObjectAttachments方法
     *
     * @return attachment pass to next hop
     */
    @Override
    public Map<String, Object> select(Invocation invocation, RpcContextAttachment clientAttachment, RpcContextAttachment serverAttachment) {
        LOG.info("进入GXPenetrateAttachmentSelector.select方法");
        String traceId = GXTraceIdContextUtils.getTraceId();
        if (CharSequenceUtil.isEmpty(traceId)) {
            traceId = RpcContext.getServerAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
        }
        if (CharSequenceUtil.isEmpty(traceId)) {
            traceId = GXTraceIdContextUtils.generateTraceId(); // "0000-0000"
        }
        return Dict.create().set("author", "塵子曦").set("SPC", "客户端").set(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
    }

    /**
     * 该方法是在该服务作为服务端使用时会被调用
     * 该方法会在{@link GXDubboServerTraceIdFilter#invoke(Invoker<?> invoker, Invocation invocation)}被调用之后调用
     *
     * @param invocation            调用者信息
     * @param clientResponseContext 客户端的响应上下文
     * @param serverResponseContext 服务端的响应上下文
     * @return Map
     */
    @Override
    public Map<String, Object> selectReverse(Invocation invocation, RpcContextAttachment clientResponseContext, RpcContextAttachment serverResponseContext) {
        // TODO 如果服务作为中间服务 则需要将TraceId再次传递
        LOG.info("进入GXPenetrateAttachmentSelector.selectReverse方法");
        String traceId = GXTraceIdContextUtils.getTraceId();
        if (CharSequenceUtil.isEmpty(traceId)) {
            traceId = RpcContext.getServerAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
        }
        if (CharSequenceUtil.isEmpty(traceId)) {
            traceId = GXTraceIdContextUtils.generateTraceId(); // "0000-0000"
        }
        return Dict.create().set("author", "塵子曦").set("SPC", "服务端").set(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
    }
}