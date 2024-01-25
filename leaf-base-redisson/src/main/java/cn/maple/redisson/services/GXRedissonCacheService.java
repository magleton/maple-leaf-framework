package cn.maple.redisson.services;

import cn.maple.core.framework.service.GXBaseCacheService;

import java.util.Map;

public interface GXRedissonCacheService extends GXBaseCacheService {
    /**
     * 获取指定桶中的所有数据
     *
     * @param bucketName 桶名字
     * @return 桶中所有的数据
     */
    Map<Object, Object> getBucketAllData(String bucketName);

    /**
     * 获取指定桶中的所有数据
     *
     * @param bucketName 桶名字
     * @param count      获取数量
     * @return 桶中所有的数据
     */
    Map<Object, Object> getBucketAllData(String bucketName, int count);

    /**
     * 获取指定桶中的所有数据
     *
     * @param bucketName 桶名字
     * @param count      获取数量
     * @param pattern    redis key pattern
     * @return 桶中所有的数据
     */
    Map<Object, Object> getBucketAllData(String bucketName, int count, String pattern);
}
