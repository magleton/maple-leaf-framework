package com.geoxus.sso.security.token;

/**
 * <p>
 * SSO Token 票据
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface GXToken {
    /**
     * 生成 Token 字符串
     */
    String getToken();
}
