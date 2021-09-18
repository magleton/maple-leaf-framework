package com.geoxus.order.cache.impl;

import com.geoxus.sso.cache.GXSSOCache;
import com.geoxus.sso.security.token.GXSSOToken;
import org.springframework.stereotype.Component;

@Component
public class GXAppSsoCacheImpl implements GXSSOCache {
    @Override
    public GXSSOToken get(String key, int expires) {
        return null;
    }

    @Override
    public boolean set(String key, GXSSOToken ssoToken, int expires) {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }
}
