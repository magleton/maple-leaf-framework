package cn.maple.core.datasource.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * redis cache decorators with logging
 *
 * @author 塵渊  britton@126.com
 */
@Slf4j
public class GXLoggingRedisCache extends LoggingCache {
    /**
     * 构造函数，二级缓存必须提供id的构造函数
     *
     * @param id 构造函数
     */
    public GXLoggingRedisCache(String id) {
        super(new GXMybatisRedissonCache(id));
    }
}
