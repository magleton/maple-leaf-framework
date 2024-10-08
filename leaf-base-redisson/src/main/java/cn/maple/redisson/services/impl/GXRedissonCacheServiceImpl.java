package cn.maple.redisson.services.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.maple.redisson.services.GXRedissonCacheService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GXRedissonCacheServiceImpl implements GXRedissonCacheService {
    @Resource
    private RedissonClient redissonClient;

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
    @Override
    public Object setCache(String bucketName, String key, Object value, int expired, TimeUnit timeUnit) {
        if (CharSequenceUtil.length(bucketName) > 64) {
            log.error("Redis的BucketName:{}太长,建议长度不超过64,请修改!", bucketName);
        }
        if (CharSequenceUtil.length(key) > 64) {
            log.error("Redis的key:{}太长,建议长度不超过64,请修改!", key);
        }
        return redissonClient.getMapCache(bucketName).put(key, value, expired, timeUnit);
    }

    /**
     * 设置缓存 有过期时间
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @param value      缓存值
     * @return 是否成功
     */
    @Override
    public Object setCache(String bucketName, String key, Object value) {
        return setCache(bucketName, key, value, 0, TimeUnit.MICROSECONDS);
    }

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
    @Override
    public Object setCache(String bucketName, String key, String value, int expired, TimeUnit timeUnit) {
        return redissonClient.getMapCache(bucketName).put(key, value, expired, timeUnit);
    }

    /**
     * 设置缓存 没有过期时间
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @param value      缓存值
     * @return 是否成功
     */
    @Override
    public Object setCache(String bucketName, String key, String value) {
        return setCache(bucketName, key, value, 0, TimeUnit.MICROSECONDS);
    }

    /**
     * 获取缓存数据
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @return 缓存值
     */
    @Override
    public Object getCache(String bucketName, String key) {
        return redissonClient.getMapCache(bucketName).get(key);
    }

    /**
     * 删除指定的缓存数据
     *
     * @param bucketName 数据桶的名字 来源于 CacheConstant
     * @param key        缓存key
     * @return 已经删除的数据
     */
    @Override
    public Object deleteCache(String bucketName, String key) {
        RMapCache<Object, Object> mapCache = redissonClient.getMapCache(bucketName);
        if (Objects.nonNull(mapCache.get(key))) {
            return mapCache.remove(key);
        }
        log.info("缓存key【{}】不存在", key);
        return null;
    }

    /**
     * 获取缓存剩余有效时长
     *
     * @param bucketName 缓存桶名字
     * @param keyName    缓存key
     * @return 剩余有效时长
     */
    @Override
    public Long getCacheRemainTimeToLive(String bucketName, String keyName) {
        return redissonClient.getMapCache(bucketName).remainTimeToLive(keyName);
    }

    /**
     * 更新缓存有效时长
     *
     * @param bucketName       缓存桶名字
     * @param keyName          缓存key
     * @param expired          需要增加的过期时间 单位:秒
     * @param refreshThreshold 刷新token缓存时间的阈值 如果小于该值 则刷新缓存的过期时间
     * @return 是否成功
     */
    @Override
    public boolean updateCacheExpiredTime(String bucketName, String keyName, Integer expired, Integer refreshThreshold) {
        Object o = redissonClient.getMapCache(bucketName).get(keyName);
        if (Objects.isNull(o)) {
            log.error("{}-{}缓存不存在", bucketName, keyName);
            return false;
        }
        long ttl = redissonClient.getMapCache(bucketName).remainTimeToLive(keyName);
        if (ttl <= refreshThreshold) {
            // <code>true</code> if key is a new key in the hash and value was set.
            // <code>false</code> if key already exists in the hash and the value was updated.
            // fastPut方法在此处会返回false 所以需要取反
            return !redissonClient.getMapCache(bucketName).fastPut(keyName, o, expired, TimeUnit.SECONDS);
        }
        return true;
    }

    /**
     * 清楚指定桶中的所有数据
     *
     * @param bucketName 缓存桶名字
     */
    @Override
    public void clear(String bucketName) {
        redissonClient.getMapCache(bucketName).clear();
    }

    /**
     * 查询桶中有多少的key
     *
     * @param bucketName 缓存桶名字
     * @return key的数量
     */
    @Override
    public Integer size(String bucketName) {
        return redissonClient.getMapCache(bucketName).size();
    }

    /**
     * 指定的桶中是否有指定的key
     *
     * @param bucketName 桶名字
     * @param key        指定的key
     * @return 是否存在
     */
    @Override
    public boolean exists(String bucketName, String key) {
        return Objects.nonNull(redissonClient.getMapCache(bucketName).get(key));
    }

    /**
     * 获取指定桶中的所有数据
     *
     * @param bucketName 桶名字
     * @param count      获取数量
     * @param pattern    redis key pattern
     * @return 桶中所有的数据
     */
    @Override
    public Map<Object, Object> getBucketAllData(String bucketName, int count, String pattern) {
        Assert.checkBetween(count, 1, 1000, "count必须在{}到{}之间.", 1, 1000);
        count = NumberUtil.min(count, 1000);
        RMapCache<Object, Object> rMapCache = redissonClient.getMapCache(bucketName);
        Set<Object> keys;
        if (CharSequenceUtil.isNotEmpty(pattern)) {
            keys = rMapCache.keySet(pattern, count);
        } else {
            keys = rMapCache.keySet(count);
        }
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyMap();
        }
        if (keys.size() > count) {
            List<Object> sub = CollUtil.sub(keys, 0, count);
            keys = CollUtil.newHashSet(sub);
        }
        return rMapCache.getAll(keys);
    }

    /**
     * 批量设置缓存数据
     *
     * @param bucketName 存储桶的名字
     * @param data       存储的数据
     */
    public void setBucketAllData(String bucketName, Map<Object, Object> data) {
        setBucketAllData(bucketName, data, 1000);
    }

    /**
     * 批量设置缓存数据
     *
     * @param bucketName 存储桶的名字
     * @param data       存储的数据
     * @param batchSize  允许插入的数据
     */
    @Override
    public void setBucketAllData(String bucketName, Map<Object, Object> data, int batchSize) {
        Assert.checkBetween(batchSize, 1, 2000, "count必须在{}到{}之间.", 1, 2000);
        batchSize = NumberUtil.min(batchSize, 2000);
        redissonClient.getMapCache(bucketName).putAll(data, batchSize);
    }

    /**
     * 删除指定Bucket中的数据
     *
     * @param bucketName 存储桶的名字
     */
    @Override
    public boolean deleteBucketAllData(String bucketName) {
        return redissonClient.getMapCache(bucketName).delete();
    }

    /**
     * 获取RedissonClient客户端实例
     *
     * @return RedissonClient
     */
    @Override
    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 获取指定桶中的所有数据
     *
     * @param bucketName 桶名字
     * @return 桶中所有的数据
     */
    @Override
    public Map<Object, Object> getBucketAllData(String bucketName) {
        return getBucketAllData(bucketName, 1000);
    }

    /**
     * 获取指定桶中的所有数据
     *
     * @param bucketName 桶名字
     * @param count      获取数量
     * @return 桶中所有的数据
     */
    @Override
    public Map<Object, Object> getBucketAllData(String bucketName, int count) {
        return getBucketAllData(bucketName, count, null);
    }
}
