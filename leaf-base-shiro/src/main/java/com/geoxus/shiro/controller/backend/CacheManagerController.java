package com.geoxus.shiro.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 清除框架缓存
 */
@Slf4j
@RestController
@RequestMapping("/cache/backend")
public class CacheManagerController {
    @Resource
    private CacheManager caffeineCache;

    /**
     * 清除框架缓存
     *
     * @return String
     */
    @RequiresRoles("super_admin")
    @GetMapping("clear")
    public String clearCache() {
        final Cache managerCache = caffeineCache.getCache("FRAMEWORK-CACHE");
        if (Objects.nonNull(managerCache)) {
            log.info("框架缓存清除成功！！");
            managerCache.clear();
        }
        return "SUCCESS";
    }
}
