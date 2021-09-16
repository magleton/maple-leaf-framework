package com.geoxus.sso.security.token;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.oauth.GXTokenManager;

/**
 * <p>
 * 刷新票据
 * </p>
 *
 * @author britton
 * @since 2021-09-16
 */
public class GXRefreshToken implements GXToken {
    private final String token;

    private GXRefreshToken(Long userId, Dict extParam) {
        token = GXTokenManager.generateUserToken(userId, extParam);
    }

    @Override
    public String getToken() {
        return null;
    }

    public String getSubject() {
        return token;
    }
}
