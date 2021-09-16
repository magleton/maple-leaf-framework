package com.geoxus.sso.security.token;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.oauth.GXTokenManager;

import java.io.Serializable;

/**
 * <p>
 * 访问票据
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public class GXAccessToken implements GXToken, Serializable {
    private String token;

    public GXAccessToken() {
        // TO DO NOTHING
    }

    public GXAccessToken(Long userId, Dict extParam) {
        this.token = GXTokenManager.generateUserToken(userId, extParam);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
