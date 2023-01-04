package cn.maple.dubbo.nacos.selector;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.PenetrateAttachmentSelector;
import org.apache.dubbo.rpc.RpcContext;

import java.util.Map;
import java.util.Objects;

@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class GXPenetrateAttachmentSelector implements PenetrateAttachmentSelector {
    /**
     * Select some attachments to pass to next hop.
     * These attachments can fetch from {@link RpcContext#getServerAttachment()} or user defined.
     * <p>
     * 该方法会将数据写入Invocation对象
     * 相当于调用Invocation.addObjectAttachments方法
     *
     * @return attachment pass to next hop
     */
    @Override
    public Map<String, Object> select() {
        String traceId = GXTraceIdContextUtils.getTraceId();
        if (CharSequenceUtil.isEmpty(traceId)) {
            traceId = RpcContext.getServerAttachment().getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
        }
        if (CharSequenceUtil.isEmpty(traceId)) {
            traceId = "0000-0000";
        }
        return Dict.create().set("author", "britton").set(GXTraceIdContextUtils.TRACE_ID_KEY, traceId);
    }
}
