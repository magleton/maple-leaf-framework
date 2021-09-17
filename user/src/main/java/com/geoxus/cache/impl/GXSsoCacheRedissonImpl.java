package com.geoxus.cache.impl;

import cn.hutool.core.convert.Convert;
import com.geoxus.sso.cache.GXSsoCache;
import com.geoxus.sso.security.token.GXSsoToken;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class GXSsoCacheRedissonImpl implements GXSsoCache {
    @Resource
    private RedissonClient redissonClient;

    @Override
    public GXSsoToken get(String key, int expires) {
        Object o = redissonClient.getMap(key + "_map").get(key);
        return Convert.convert(GXSsoToken.class, o);
    }

    @Override
    public boolean set(String key, GXSsoToken ssoToken, int expires) {
        redissonClient.getMap(key + "_map").put(key, ssoToken);
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }
}
