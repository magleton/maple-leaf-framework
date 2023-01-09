package cn.maple.sso.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXBaseCacheService;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.sso.properties.GXSSOConfigProperties;
import cn.maple.sso.service.GXTokenConfigService;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
     * @param key      关键词
     * @param expires  过期时间（延时心跳时间）
     * @param ssoToken cookie中存储的ssoToken
     * @return SSO票据
     */
    @SuppressWarnings("all")
    default Dict get(int expires, Dict ssoToken) {
        GXBaseCacheService cacheService = GXSpringContextUtils.getBean(GXBaseCacheService.class);
        GXTokenConfigService tokenConfigService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        assert tokenConfigService != null;
        assert cacheService != null;
        String cacheKey = tokenConfigService.getTokenCacheKey(ssoToken.getLong("userId"));
        Object o = cacheService.getCache(tokenConfigService.getCacheBucketName(), cacheKey);
        if (Objects.nonNull(o)) {
            return Convert.convert(Dict.class, o);
        }
        // 1、解码本地token
        String tokenSecret = tokenConfigService.getTokenSecret();
        GXSSOConfigProperties ssoProperties = GXSpringContextUtils.getBean(GXSSOConfigProperties.class);
        assert ssoProperties != null;
        String tokenName = ssoProperties.getConfig().getTokenName();
        Dict tokenData = GXCurrentRequestContextUtils.getLoginCredentials(tokenName, tokenSecret);
        if (CollUtil.isEmpty(tokenData)) {
            throw new GXBusinessException("token已经失效,请重新登录!", HttpStatus.HTTP_NOT_AUTHORITATIVE);
        }
        // 2、调用用户服务的验证用户是否有效
        boolean b = tokenConfigService.checkLoginStatus();
        if (!b) {
            throw new GXBusinessException("登录状态已经失效,请重新登录!", HttpStatus.HTTP_NOT_AUTHORITATIVE);
        }
        return Convert.convert(Dict.class, tokenData);
    }

    /**
     * 设置SSO票据
     *
     * @param key            关键词
     * @param ssoToken       SSO票据
     * @param expires        过期时间
     * @param cookieSSOToken cookie中存储的ssoToken
     */
    @SuppressWarnings("all")
    default boolean set(Dict ssoToken, int expires, Dict cookieSSOToken) {
        GXBaseCacheService cacheService = GXSpringContextUtils.getBean(GXBaseCacheService.class);
        GXTokenConfigService tokenConfigService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        assert tokenConfigService != null;
        assert cacheService != null;
        String cacheKey = CharSequenceUtil.format("{}{}", tokenConfigService.getTokenCachePrefix(), ssoToken.getLong("userId"));
        String cacheBucketName = tokenConfigService.getCacheBucketName();
        cacheService.setCache(cacheBucketName, cacheKey, JSONUtil.toJsonStr(ssoToken), expires, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 删除SSO票据
     * <p>
     *
     * @param key 关键词
     */
    @SuppressWarnings("all")
    default boolean delete(Dict ssoToken) {
        GXBaseCacheService cacheService = GXSpringContextUtils.getBean(GXBaseCacheService.class);
        GXTokenConfigService tokenConfigService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        assert tokenConfigService != null;
        assert cacheService != null;
        String cacheKey = CharSequenceUtil.format("{}{}", tokenConfigService.getTokenCachePrefix(), ssoToken.getLong("userId"));
        String cacheBucketName = tokenConfigService.getCacheBucketName();
        Object o = cacheService.deleteCache(cacheBucketName, cacheKey);
        return Objects.nonNull(o);
    }
}
