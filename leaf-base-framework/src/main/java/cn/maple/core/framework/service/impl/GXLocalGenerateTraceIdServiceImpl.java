package cn.maple.core.framework.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.maple.core.framework.service.GXGenerateTraceIdService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingClass(value = "org.apache.skywalking.apm.toolkit.trace.TraceContext")
public class GXLocalGenerateTraceIdServiceImpl implements GXGenerateTraceIdService {
    /**
     * 生成TraceId
     *
     * @return TraceId
     */
    @Override
    public String generateTraceId() {
        return IdUtil.fastSimpleUUID();
    }
}
