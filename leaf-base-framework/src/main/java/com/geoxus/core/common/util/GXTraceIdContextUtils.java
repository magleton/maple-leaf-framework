package com.geoxus.core.common.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;

public class GXTraceIdContextUtils {
    public static final String TRACE_ID_KEY = "TraceId";

    private GXTraceIdContextUtils() {
    }

    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        return traceId == null ? "" : traceId;
    }

    public static void setTraceId(String traceId) {
        if (CharSequenceUtil.isNotEmpty(traceId)) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    public static void removeTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    public static void clearTraceId() {
        MDC.clear();
    }

    public static class GXTraceIdGenerator {
        private GXTraceIdGenerator() {
        }

        /**
         * 生成traceId
         *
         * @return TraceId 基于UUID
         */
        public static String getTraceId() {
            return IdUtil.randomUUID();
        }
    }
}
