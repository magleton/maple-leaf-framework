package cn.maple.core.framework.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import com.github.benmanes.caffeine.cache.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class GXCaffeineCacheUtils {
    private static final Map<String, Cache<?, ?>> CACHE_MAP = new ConcurrentHashMap<>();

    private static final Map<String, LoadingCache<?, ?>> LOADING_CACHE_MAP = new ConcurrentHashMap<>();

    private static final Map<String, AsyncCache<?, ?>> ASYNC_CACHE_MAP = new ConcurrentHashMap<>();

    private static final Map<String, AsyncLoadingCache<?, ?>> ASYNC_LOADING_CACHE_MAP = new ConcurrentHashMap<>();

    private static final Map<String, Caffeine<?, ?>> CAFFEINE_MAP = new ConcurrentHashMap<>();

    private GXCaffeineCacheUtils() {
    }

    /**
     * 获取Caffeine的Cache对象
     *
     * @param configNameKey 缓存的KEY
     * @return Cache
     */
    public static <K, V> Cache<K, V> getCaffeineCache(String configNameKey) {
        final String cacheName = configNameKey + "caffeine-cache";
        Cache<?, ?> cache = CACHE_MAP.get(cacheName);
        if (Objects.isNull(cache)) {
            Caffeine<K, V> caffeine = getCaffeine(configNameKey);
            cache = caffeine.build();
            CACHE_MAP.put(cacheName, cache);
        }
        return Convert.convert(new TypeReference<>() {
        }, cache);
    }

    /**
     * 通过CacheLoader获取同步Cache对象
     *
     * @param configNameKey 缓存名字的KEY
     * @param cacheLoader   CacheLoader
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> getCaffeineCache(String configNameKey, CacheLoader<K, V> cacheLoader) {
        final String cacheName = configNameKey + "cache-loader-caffeine-cache";
        LoadingCache<?, ?> cache = LOADING_CACHE_MAP.get(cacheName);
        if (Objects.isNull(cache)) {
            Caffeine<K, V> caffeine = getCaffeine(configNameKey);
            cache = caffeine.build(cacheLoader);
            LOADING_CACHE_MAP.put(cacheName, cache);
        }
        return Convert.convert(new TypeReference<>() {
        }, cache);
    }

    /**
     * 获取异步Cache对象
     *
     * @param configNameKey 缓存名字的KEY
     * @return AsyncCache
     */
    public static <K, V> AsyncCache<K, V> getAsyncCaffeine(String configNameKey) {
        final String cacheName = configNameKey + "async-caffeine-cache";
        AsyncCache<?, ?> cache = ASYNC_CACHE_MAP.get(cacheName);
        if (Objects.isNull(cache)) {
            Caffeine<K, V> caffeine = getCaffeine(configNameKey);
            cache = caffeine.buildAsync();
            ASYNC_CACHE_MAP.put(cacheName, cache);
        }
        return Convert.convert(new TypeReference<>() {
        }, cache);
    }

    /**
     * 通过指定CacheLoader来获取指定的Cache对象
     *
     * @param cacheNameKey     缓存名字
     * @param asyncCacheLoader CacheLoader
     * @return AsyncLoadingCache
     */
    public static <K, V> AsyncLoadingCache<K, V> getAsyncCaffeine(String cacheNameKey, AsyncCacheLoader<K, V> asyncCacheLoader) {
        final String cacheName = cacheNameKey + "origin-async-cache-loader-caffeine-cache";
        AsyncLoadingCache<?, ?> cache = ASYNC_LOADING_CACHE_MAP.get(cacheName);
        if (Objects.nonNull(cache)) {
            Caffeine<K, V> caffeine = getCaffeine(cacheNameKey);
            cache = caffeine.buildAsync(asyncCacheLoader);
            ASYNC_LOADING_CACHE_MAP.put(cacheName, cache);
        }
        return Convert.convert(new TypeReference<>() {
        }, cache);
    }

    /**
     * 获取Caffeine对象
     *
     * @param cacheNameKey 缓存名字的key
     * @return Caffeine
     */
    private static <K, V> Caffeine<K, V> getCaffeine(String cacheNameKey) {
        String spec = GXCommonUtils.getEnvironmentValue(cacheNameKey, String.class);
        if (CharSequenceUtil.isBlank(spec)) {
            spec = "maximumSize=1024";
        }
        final String cacheName = cacheNameKey + "caffeine-obj";
        Caffeine<?, ?> caffeine = CAFFEINE_MAP.get(cacheName);
        if (Objects.isNull(caffeine)) {
            caffeine = Caffeine.from(spec);
            CAFFEINE_MAP.put(cacheName, caffeine);
        }
        return Convert.convert(new TypeReference<>() {
        }, caffeine);
    }
}
