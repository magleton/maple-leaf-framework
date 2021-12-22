package cn.maple.core.framework.config.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GXCaffeineCacheConfig {
    @Value("${spring.cache.cache-names:}")
    private String cacheNames;

    @Value("${spring.cache.caffeine.spec:}")
    private String cacheSpec;

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean("caffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        List<String> lastCacheNames = new ArrayList<>(16);
        if (CharSequenceUtil.isNotEmpty(cacheNames)) {
            lastCacheNames.addAll(CharSequenceUtil.split(cacheNames, ","));
        }
        lastCacheNames.addAll(CollUtil.newArrayList("FRAMEWORK-CACHE", "__DEFAULT__", "BIZ-CACHE"));
        caffeineCacheManager.setCacheNames(lastCacheNames);
        caffeineCacheManager.setAllowNullValues(false);
        if (CharSequenceUtil.isEmpty(cacheSpec)) {
            cacheSpec = "initialCapacity=50,maximumSize=10000,expireAfterWrite=10s,recordStats,softValues";
        }
        caffeineCacheManager.setCaffeineSpec(CaffeineSpec.parse(cacheSpec));
        return caffeineCacheManager;
    }
}
