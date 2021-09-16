package com.geoxus.sso.cache;

import com.geoxus.sso.security.token.GXSsoToken;

/**
 * <p>
 * SSO 缓存接口
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface GXSsoCache {

    /**
     * <p>
     * 根据key获取SSO票据
     * </p>
     * <p>
     * 如果缓存服务宕机，返回 token 设置 flag 为 Token.FLAG_CACHE_SHUT
     * </p>
     *
     * @param key     关键词
     * @param expires 过期时间（延时心跳时间）
     * @return SSO票据
     */
    GXSsoToken get(String key, int expires);

    /**
     * 设置SSO票据
     *
     * @param key      关键词
     * @param ssoToken SSO票据
     * @param expires  过期时间
     */
    boolean set(String key, GXSsoToken ssoToken, int expires);

    /**
     * 删除SSO票据
     * <p>
     *
     * @param key 关键词
     */
    boolean delete(String key);
}
