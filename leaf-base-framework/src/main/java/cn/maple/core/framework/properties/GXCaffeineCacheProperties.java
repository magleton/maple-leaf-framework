package cn.maple.core.framework.properties;

import cn.maple.core.framework.dto.GXBaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXCaffeineCacheProperties extends GXBaseData {
    /**
     * 初始的缓存空间大小
     */
    private Integer initialCapacity;

    /**
     * 缓存的最大条数
     */
    private Long maximumSize;

    /**
     * 缓存的最大权重
     */
    private Long maximumWeight;

    /**
     * 最后一次写入或访问后经过固定时间过期
     * 单位  秒
     */
    private Integer expireAfterAccess;

    /**
     * 最后一次写入后经过固定时间过期
     * 单位  秒
     */
    private Integer expireAfterWrite;

    /**
     * 创建缓存或者最近一次更新缓存后经过固定的时间间隔,刷新缓存
     * 单位  秒
     */
    private Integer refreshAfterWrite;

    /**
     * 打开key的弱引用
     */
    private Boolean weakKeys = Boolean.FALSE;

    /**
     * 打开value的弱引用
     */
    private Boolean weakValues = Boolean.FALSE;

    /**
     * 打开value的软引用
     */
    private Boolean softValues = Boolean.FALSE;

    /**
     * 开发统计功能
     */
    private Boolean recordStats = Boolean.FALSE;
}
