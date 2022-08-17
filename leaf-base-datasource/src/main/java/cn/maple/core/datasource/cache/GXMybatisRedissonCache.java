package cn.maple.core.datasource.cache;

import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.redisson.services.GXRedissonCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Mybatis 使用Redisson作为二级缓存
 *
 * @author 塵渊  britton@126.com
 */
@Slf4j
public class GXMybatisRedissonCache implements Cache {
    /**
     * Redisson 客户端
     */
    private static final RedissonClient redissonClient;

    /**
     * 缓存服务
     */
    private static final GXRedissonCacheService redissonCacheService;

    static {
        redissonClient = GXSpringContextUtils.getBean(RedissonClient.class);
        redissonCacheService = GXSpringContextUtils.getBean(GXRedissonCacheService.class);
    }


    /**
     * cache instance id
     */
    private final String id;

    /**
     * 缓存刷新间隔,单位为毫秒
     * flushInterval参数 note : 自定义cache无法使用默认的flushInterval
     */
    private Integer flushInterval = 0;

    public GXMybatisRedissonCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    public void setFlushInterval(Integer flushInterval) {
        this.flushInterval = flushInterval;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Put query result to redis
     */
    @Override
    public void putObject(Object key, Object value) {
        assert redissonCacheService != null;
        redissonCacheService.setCache(getId(), key.toString(), value);
        if (flushInterval > 0) {
            redissonCacheService.setCache(getId(), key.toString(), value, flushInterval, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Get cached query result from redis
     */
    @Override
    public Object getObject(Object key) {
        assert redissonCacheService != null;
        return redissonCacheService.getCache(getId(), key.toString());
    }

    /**
     * Remove cached query result from redis
     */
    @Override
    public Object removeObject(Object key) {
        assert redissonCacheService != null;
        return redissonCacheService.deleteCache(getId(), key.toString());
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        assert redissonCacheService != null;
        redissonCacheService.clear(getId());
    }

    /**
     * This method is not used
     */
    @Override
    public int getSize() {
        assert redissonCacheService != null;
        return redissonCacheService.size(getId());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        assert redissonClient != null;
        return redissonClient.getReadWriteLock("MyBatis:LOCK");
    }
}
