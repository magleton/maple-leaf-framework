package cn.maple.core.framework.util;

import cn.hutool.core.text.CharSequenceUtil;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * GXThreadMdcUtil
 *
 * @author gapleaf@163.com
 */
@SuppressWarnings("unused")
public class GXMdcThreadUtils {
    private GXMdcThreadUtils() {

    }

    public static void setTraceIdIfAbsent() {
        if (CharSequenceUtil.isBlank(GXTraceIdContextUtils.getTraceId())) {
            GXTraceIdContextUtils.setTraceId(GXTraceIdContextUtils.generateTraceId());
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
