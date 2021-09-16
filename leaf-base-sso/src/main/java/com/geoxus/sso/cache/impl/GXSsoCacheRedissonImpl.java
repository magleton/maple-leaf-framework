package com.geoxus.sso.cache.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.sso.cache.GXSsoCache;
import com.geoxus.sso.enums.GXTokenFlag;
import com.geoxus.sso.security.token.GXSsoToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GXSsoCacheRedissonImpl implements GXSsoCache {
    @Override
    public GXSsoToken get(String key, int expires) {
        GXSsoToken ssoToken = new GXSsoToken();
        ssoToken.setFlag(GXTokenFlag.CACHE_SHUT);
        ssoToken.setData(Dict.create().set("username", "jack"));
        return ssoToken;
    }

    @Override
    public boolean set(String key, GXSsoToken ssoToken, int expires) {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }
}
