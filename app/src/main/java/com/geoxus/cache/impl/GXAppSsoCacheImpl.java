package com.geoxus.cache.impl;

import com.geoxus.sso.cache.GXSsoCache;
import com.geoxus.sso.security.token.GXSsoToken;
import org.springframework.stereotype.Component;

@Component
public class GXAppSsoCacheImpl implements GXSsoCache {
    @Override
    public GXSsoToken get(String key, int expires) {
        return null;
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
