package com.geoxus.core.common.util;

import cn.hutool.core.convert.Convert;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import org.redisson.api.*;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class GXRedisUtils {
    @GXFieldCommentAnnotation(zhDesc = "Logger对象")
    private static final Logger logger;

    @GXFieldCommentAnnotation("计数器缓存的名字")
    private static final String COUNTER_MAP_CACHE_NAME = "counter_map_cache_name";

    static {
        logger = GXCommonUtils.getLogger(GXRedisUtils.class);
    }

    private GXRedisUtils() {
    }

    /**
     * 设置数据
     *
     * @param key      KEY
     * @param value    数据
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return Object
     */
    public static Object set(String key, String value, int expire, TimeUnit timeUnit) {
        final RMap<Object, Object> rMap = getRedissonClient().getMap(key);
        if (expire > 0) {
            rMap.expire(expire, timeUnit);
        }
        return rMap.put(key, value);
    }

    /**
     * 获取数据
     *
     * @param key   数据key
     * @param clazz 返回数据的类型
     * @return Object
     */
    public static <R> R get(String key, Class<R> clazz) {
        final RMap<Object, Object> rMap = getRedissonClient().getMap(key);
        return Convert.convert(clazz, rMap.get(key));
    }

    /**
     * 删除数据
     *
     * @param key 数据key
     * @return boolean
     */
    public static boolean delete(String key) {
        final RMap<Object, Object> rMap = getRedissonClient().getMap(key);
        return null != rMap.remove(key);
    }

    /**
     * 获取并操作计数器的值
     * 返回+1后的计数器的值
     *
     * @param key      数据key
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return long
     */
    public static long getCounter(String key, int expire, TimeUnit timeUnit) {
        final RLock rLock = getLock(key);
        RMapCache<Object, Object> rMapCache = getRedissonClient().getMapCache(COUNTER_MAP_CACHE_NAME);
        try {
            rLock.lock();
            Object oldCount = rMapCache.get(key);
            if (null == oldCount) {
                long counter = 1;
                rMapCache.put(key, counter, expire, timeUnit);
                return counter;
            }
            long counter = (long) oldCount + 1L;
            rMapCache.put(key, counter);
            return counter;
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获当前计数器的值,不会修改旧值
     *
     * @param key 计数器的key
     * @return long
     */
    public static long getCounter(String key) {
        RMapCache<Object, Object> rMapCache = getRedissonClient().getMapCache(COUNTER_MAP_CACHE_NAME);
        final Object o = rMapCache.get(key);
        if (null == o) {
            return -1;
        }
        return Convert.convert(Long.class, o);
    }

    /**
     * 获取Redis锁
     *
     * @param lockName 锁的名字
     * @return RLock
     */
    public static RLock getLock(String lockName) {
        return getRedissonClient().getLock("lock:" + lockName);
    }

    /**
     * API请求限流,在单位时间内只能请求多少次
     *
     * @param name             限流器的名字
     * @param rate             频率
     * @param rateInterval     时间
     * @param rateIntervalUnit 时间单位
     * @return boolean
     */
    public static boolean throttling(String name, int rate, int rateInterval, RateIntervalUnit rateIntervalUnit) {
        final RRateLimiter rateLimiter = getRedissonClient().getRateLimiter(name);
        return rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, rateIntervalUnit);
    }

    /**
     * 获取RedissonClient对象
     *
     * @return RedissonClient
     */
    public static RedissonClient getRedissonClient() {
        return GXSpringContextUtils.getBean(RedissonClient.class);
    }
}