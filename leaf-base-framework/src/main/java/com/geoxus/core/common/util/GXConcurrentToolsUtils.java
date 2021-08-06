package com.geoxus.core.common.util;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 并发请求任务工具类
 */
public class GXConcurrentToolsUtils {
    /**
     * 标识特殊值 并行获取失败是会设置该值
     */
    public static final String FLAG_SPECIAL_VALUE = "BRT";

    /**
     * 默认超时时间是5秒钟
     */
    private static final int DEFAULT_TIME_OUT = 5;

    /**
     * 日志对象
     */
    private static final Logger LOG = GXCommonUtils.getLogger(GXConcurrentToolsUtils.class);

    /**
     * 线程池对象
     */
    private static final ExecutorService EXECUTOR_SERVICE = ExecutorBuilder.create()
            .setCorePoolSize(Runtime.getRuntime().availableProcessors())
            .setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2)
            .setThreadFactory(ThreadFactoryBuilder.create().setNamePrefix("gx-concurrent-thread-pool").build())
            .setWorkQueue(new LinkedBlockingQueue<>(10000))
            .build();

    /**
     * 私有构造函数
     */
    private GXConcurrentToolsUtils() {

    }

    /**
     * 将任务对象包装为一个CompletableFuture对象
     *
     * @param callable  任务对象
     * @param results   承载计算结果的容器
     * @param resultKey 结果关联的KEY
     * @param <T>       泛型类名
     * @return T
     */
    public static <T> CompletableFuture<T> composerFuture(Supplier<T> callable, ConcurrentMap<String, Object> results, String resultKey) {
        final CompletableFuture<T> future = CompletableFuture.supplyAsync(callable, EXECUTOR_SERVICE);
        future.whenComplete((t, ex) -> results.putIfAbsent(resultKey, null));
        future.handleAsync((t, ex) -> {
            if (Objects.nonNull(ex)) {
                LOG.error("异常发生了 {} ", ex.getMessage());
            } else {
                LOG.error("异常信息为null");
            }
            return t;
        });
        future.exceptionally(ex -> {
            results.putIfAbsent(resultKey, FLAG_SPECIAL_VALUE);
            future.completeExceptionally(ex);
            return null;
        });
        return future;
    }

    /**
     * @param completableFutures CompletableFuture对象列表
     * @return void
     */
    public static void allOf(List<CompletableFuture<?>> completableFutures, int timeOut, TimeUnit unit) {
        try {
            CompletableFuture.allOf(completableFutures.stream().toArray(value -> new CompletableFuture[completableFutures.size()])).get(timeOut, unit);
        } catch (InterruptedException e) {
            LOG.error("InterruptedException: ", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException | TimeoutException e) {
            LOG.error("并发获取数据出错 {}", e.getMessage());
            if (e instanceof TimeoutException) {
                completableFutures.forEach(f -> f.cancel(true));
            }
        }
    }

    public static void allOf(List<CompletableFuture<?>> completableFutures) {
        allOf(completableFutures, DEFAULT_TIME_OUT, TimeUnit.SECONDS);
    }
}
