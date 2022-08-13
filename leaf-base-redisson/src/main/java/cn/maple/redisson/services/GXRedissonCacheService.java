package cn.maple.redisson.services;

import java.util.concurrent.TimeUnit;

public interface GXRedissonCacheService {
    /**
     * 设置缓存 有过期时间
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @param value      缓存值
     * @param expired    过期时间
     * @param timeUnit   时间单位
     * @return 是否成功
     */
    Object setCache(String bucketName, String key, Object value, int expired, TimeUnit timeUnit);

    /**
     * 设置缓存 有过期时间
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @param value      缓存值
     * @return 是否成功
     */
    Object setCache(String bucketName, String key, Object value);

    /**
     * 设置缓存 没有过期时间
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @param value      缓存值
     * @param expired    过期时间
     * @param timeUnit   时间单位
     * @return 是否成功
     */
    Object setCache(String bucketName, String key, String value, int expired, TimeUnit timeUnit);

    /**
     * 设置缓存 没有过期时间
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @param value      缓存值
     * @return 是否成功
     */
    Object setCache(String bucketName, String key, String value);

    /**
     * 获取缓存数据
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @return 缓存值
     */
    Object getCache(String bucketName, String key);

    /**
     * 删除指定的缓存数据
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @return 已经删除的数据
     */
    Object deleteCache(String bucketName, String key);

    /**
     * 获取缓存剩余有效时长
     *
     * @param bucketName 缓存桶名字
     * @param keyName    缓存key
     * @return 剩余有效时长
     */
    Long getCacheRemainTimeToLive(String bucketName, String keyName);

    /**
     * 跟新缓存有效时长
     *
     * @param bucketName       缓存桶名字
     * @param keyName          缓存key
     * @param expired          过期时间 单位: 秒
     * @param refreshThreshold 刷新token缓存时间的阈值 如果小于该值 则刷新缓存的过期时间
     * @return 是否成功
     */
    boolean updateCacheExpiredTime(String bucketName, String keyName, Integer expired, Integer refreshThreshold);
}
