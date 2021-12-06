package cn.maple.skywalking.service.impl;

import cn.maple.core.framework.service.GXGenerateTraceIdService;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnClass(name = "org.apache.skywalking.apm.toolkit.trace.TraceContext")
public class GXSkyWalkingGenerateTraceIdServiceImpl implements GXGenerateTraceIdService {
    /**
     * 生成TraceId
     *
     * @return TraceId
     */
    @Override
    public String generateTraceId() {
        return TraceContext.traceId();
    }
}
