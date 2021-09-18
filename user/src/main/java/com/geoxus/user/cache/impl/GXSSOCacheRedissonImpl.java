package com.geoxus.user.cache.impl;

import cn.hutool.core.convert.Convert;
import com.geoxus.sso.cache.GXSSOCache;
import com.geoxus.sso.security.token.GXSSOToken;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class GXSSOCacheRedissonImpl implements GXSSOCache {
    @Resource
    private RedissonClient redissonClient;

    @Override
    public GXSSOToken get(String key, int expires) {
        Object o = redissonClient.getMap(key + "_map").get(key);
        return Convert.convert(GXSSOToken.class, o);
    }

    @Override
    public boolean set(String key, GXSSOToken ssoToken, int expires) {
        redissonClient.getMap(key + "_map").put(key, ssoToken);
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }
}
