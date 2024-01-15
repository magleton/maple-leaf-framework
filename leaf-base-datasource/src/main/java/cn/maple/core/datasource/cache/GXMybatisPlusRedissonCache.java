package cn.maple.core.datasource.cache;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.redisson.services.GXRedissonCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.cache.Cache;
import org.redisson.api.RedissonClient;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Mybatis Plus 使用Redisson作为二级缓存
 *
 * @author 塵渊  britton@126.com
 */
@Slf4j
public class GXMybatisPlusRedissonCache implements Cache {
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

    public GXMybatisPlusRedissonCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        log.info("MP二级缓存id : {}", id);
        try {
            flushInterval = Math.toIntExact(AnnotationUtil.getAnnotation(Class.forName(id), CacheNamespace.class).flushInterval());
            this.id = id;
        } catch (ClassNotFoundException e) {
            throw new GXBusinessException(e.getMessage(), e);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Put query result to redis
     */
    @Override
    public void putObject(@NotNull Object key, @NotNull Object value) {
        assert redissonCacheService != null;
        if (Objects.nonNull(value)) {
            String storeKey = md5Encrypt(key);
            log.info("MP->>向【{}】缓存桶中存入缓存key:{}", id, storeKey);
            if (flushInterval > 0) {
                redissonCacheService.setCache(getId(), storeKey, value, flushInterval, TimeUnit.MILLISECONDS);
            } else {
                redissonCacheService.setCache(getId(), storeKey, value);
            }
        }
    }

    /**
     * Get cached query result from redis
     */
    @Override
    public Object getObject(@NotNull Object key) {
        assert redissonCacheService != null;
        String storeKey = md5Encrypt(key);
        log.info("MP->>从缓存桶【{}】中获取缓存key:{}", id, storeKey);
        Object cache = redissonCacheService.getCache(getId(), storeKey);
        if (Objects.isNull(cache)) {
            log.info("MP->>缓存桶【{}】中缓存key:{}不存在", id, storeKey);
        }
        return cache;
    }

    /**
     * Remove cached query result from redis
     */
    @Override
    public Object removeObject(@NotNull Object key) {
        assert redissonCacheService != null;
        String storeKey = md5Encrypt(key);
        log.info("MP->>从缓存桶【{}】中删除缓存key:{}", id, storeKey);
        return redissonCacheService.deleteCache(getId(), storeKey);
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        assert redissonCacheService != null;
        log.info("MP->>将缓存桶【{}】中的数据清空", id);
        redissonCacheService.clear(getId());
    }

    /**
     * This method is not used
     */
    @Override
    public int getSize() {
        assert redissonCacheService != null;
        log.info("MyBatisPlus->>获取缓存桶【{}】中缓存数据的数量", id);
        return redissonCacheService.size(getId());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        assert redissonClient != null;
        return redissonClient.getReadWriteLock("MP:LOCK:" + getId());
    }

    /**
     * MD5加密存储key,可以节约内存
     *
     * @param key 待加密的key
     * @return 加密之后的key
     */
    private String md5Encrypt(Object key) {
        return DigestUtils.md5DigestAsHex(key.toString().getBytes());
    }
}
