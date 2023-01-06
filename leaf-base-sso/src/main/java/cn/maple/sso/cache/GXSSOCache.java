package cn.maple.sso.cache;

import cn.maple.sso.security.token.GXSSOToken;

/**
 * <p>
 * SSO 缓存接口
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface GXSSOCache {
    /**
     * <p>
     * 根据key获取SSO票据
     * </p>
     * <p>
     * 如果缓存服务宕机，返回 token 设置 flag 为 Token.FLAG_CACHE_SHUT
     * </p>
     *
     * @param key            关键词
     * @param expires        过期时间（延时心跳时间）
     * @param cookieSSOToken cookie中存储的ssoToken
     * @return SSO票据
     */
    GXSSOToken get(String key, int expires, GXSSOToken cookieSSOToken);

    /**
     * 设置SSO票据
     *
     * @param key            关键词
     * @param ssoToken       SSO票据
     * @param expires        过期时间
     * @param cookieSSOToken cookie中存储的ssoToken
     */
    boolean set(String key, GXSSOToken ssoToken, int expires, GXSSOToken cookieSSOToken);

    /**
     * 删除SSO票据
     * <p>
     *
     * @param key 关键词
     */
    boolean delete(String key);
}
