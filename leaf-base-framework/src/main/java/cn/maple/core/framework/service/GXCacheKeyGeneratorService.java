package cn.maple.core.framework.service;

/**
 * 自定义缓存的生成器
 *
 * @author 塵渊  britton@126.com
 */
public interface GXCacheKeyGeneratorService {
    /**
     * 生成缓存key
     *
     * @param param 参数
     * @return 缓存key
     */
    default String cacheKey(Object... param) {
        return "";
    }

    /**
     * 缓存的条件
     *
     * @param param 参数
     * @return 是否缓存
     */
    default Boolean cacheCondition(Object... param) {
        return Boolean.TRUE;
    }
}
