package cn.maple.core.framework.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Objects;

public class GXTraceIdContextUtils {
    /**
     * TraceId的字段名字
     */
    public static final String TRACE_ID_KEY = "X-B3-TraceId";

    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXTraceIdContextUtils.class);

    /**
     * 私有化构造函数
     */
    private GXTraceIdContextUtils() {
    }

    /**
     * 获取TraceId
     *
     * @return String
     */
    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        return Objects.isNull(traceId) ? "" : traceId;
    }

    /**
     * 设置TraceId
     *
     * @param traceId 需要设置的TraceId
     */
    public static void setTraceId(String traceId) {
        if (CharSequenceUtil.isNotEmpty(traceId)) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    /**
     * 移除指定的TraceId
     */
    public static void removeTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        if (Objects.isNull(traceId)) {
            return;
        }
        MDC.remove(TRACE_ID_KEY);
    }

    /**
     * 清空TraceId
     */
    public static void clearTraceId() {
        MDC.clear();
    }

    /**
     * 生成TraceId
     *
     * @return String
     */
    public static String generateTraceId() {
        return GXTraceIdGenerator.generateTraceId();
    }

    /**
     * TraceId生成器
     */
    private static class GXTraceIdGenerator {
        private GXTraceIdGenerator() {
        }

        /**
         * 生成traceId
         *
         * @return TraceId 基于UUID
         */
        public static String generateTraceId() {
            return IdUtil.fastSimpleUUID();
        }
    }
}
