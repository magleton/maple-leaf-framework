package com.geoxus.core.datasource.config;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 多数据源线程上下文
 */
public class GXDynamicContextHolder {
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = ThreadLocal.withInitial(ArrayDeque::new);

    private GXDynamicContextHolder() {

    }

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return CONTEXT_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     *
     * @param dataSourceName 数据源名称
     */
    public static void push(String dataSourceName) {
        CONTEXT_HOLDER.get().push(dataSourceName);
    }

    /**
     * 清空当前线程数据源
     */
    public static void poll() {
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }
}
