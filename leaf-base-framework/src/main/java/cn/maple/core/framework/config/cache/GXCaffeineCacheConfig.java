package cn.maple.core.framework.config.cache;

import cn.hutool.core.collection.CollUtil;
import cn.maple.core.framework.properties.GXCaffeineCacheManagerProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class GXCaffeineCacheConfig {
    @Resource
    private GXCaffeineCacheManagerProperties caffeineCacheManagerProperties;

    /**
     * 配置Caffeine缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean("caffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManagerProperties.getConfig().forEach((name, caffeineCacheProperties) -> {
            Integer initialCapacity = caffeineCacheProperties.getInitialCapacity();
            Integer expireAfterAccess = caffeineCacheProperties.getExpireAfterAccess();
            Long maximumSize = caffeineCacheProperties.getMaximumSize();
            boolean recordStats = caffeineCacheProperties.getRecordStats();
            boolean softValues = caffeineCacheProperties.getSoftValues();
            Caffeine<Object, Object> caffeine = Caffeine.newBuilder().initialCapacity(initialCapacity).expireAfterAccess(expireAfterAccess, TimeUnit.SECONDS).maximumSize(maximumSize);
            if (recordStats) {
                caffeine.recordStats();
            }
            if (softValues) {
                caffeine.softValues();
            }
            caffeineCacheManager.registerCustomCache(name, caffeine.build());
        });

        // 新增默认的缓存
        CollUtil.newArrayList("FRAMEWORK-CACHE", "__DEFAULT__", "BIZ-CACHE").forEach(name -> {
            Caffeine<Object, Object> caffeine = Caffeine.newBuilder().initialCapacity(50).expireAfterAccess(86400, TimeUnit.SECONDS).maximumSize(10000).softValues().recordStats();
            caffeineCacheManager.registerCustomCache(name, caffeine.build());
        });
        return caffeineCacheManager;
    }
}
