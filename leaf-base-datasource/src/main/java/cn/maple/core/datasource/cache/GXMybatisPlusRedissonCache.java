package cn.maple.core.datasource.cache;

import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.redisson.services.GXRedissonCacheService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.redisson.api.RedissonClient;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotNull;
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
    @Setter
    private Integer flushInterval = 0;

    public GXMybatisPlusRedissonCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        log.info("MyBatis Plus二级缓存id : {}", id);
        this.id = id;
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
        log.info("MyBatisPlus将数据存入缓存");
        assert redissonCacheService != null;
        String storeKey = md5Encrypt(key);
        if (flushInterval > 0) {
            redissonCacheService.setCache(getId(), storeKey, value, flushInterval, TimeUnit.MILLISECONDS);
        } else {
            redissonCacheService.setCache(getId(), storeKey, value);
        }
    }

    /**
     * Get cached query result from redis
     */
    @Override
    public Object getObject(@NotNull Object key) {
        log.info("MyBatisPlus获取缓存数据");
        assert redissonCacheService != null;
        String storeKey = md5Encrypt(key);
        return redissonCacheService.getCache(getId(), storeKey);
    }

    /**
     * Remove cached query result from redis
     */
    @Override
    public Object removeObject(@NotNull Object key) {
        log.info("MyBatisPlus删除缓存数据");
        assert redissonCacheService != null;
        String storeKey = md5Encrypt(key);
        return redissonCacheService.deleteCache(getId(), storeKey);
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        log.info("MyBatisPlus清空缓存数据");
        assert redissonCacheService != null;
        redissonCacheService.clear(getId());
    }

    /**
     * This method is not used
     */
    @Override
    public int getSize() {
        log.info("MyBatisPlus获取缓存数据的数量");
        assert redissonCacheService != null;
        return redissonCacheService.size(getId());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        assert redissonClient != null;
        return redissonClient.getReadWriteLock("MyBatis:LOCK");
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
