package cn.maple.sso.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXBaseCacheService;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.sso.properties.GXSSOConfigProperties;
import cn.maple.sso.service.GXTokenConfigService;

import java.util.Objects;
import java.util.Optional;
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
        Long userId = Optional.ofNullable(ssoToken.getLong(GXTokenConstant.TOKEN_USER_ID_FIELD_NAME)).orElse(ssoToken.getLong(GXTokenConstant.TOKEN_ADMIN_ID_FIELD_NAME));
        String cacheKey = tokenConfigService.getTokenCacheKey(userId, ssoToken);
        String cacheBucketName = tokenConfigService.getCacheBucketName();
        Object o = cacheService.getCache(cacheBucketName, cacheKey);
        if (Objects.nonNull(o) && JSONUtil.isTypeJSON(o.toString())) {
            // 如果缓存中的SSOToken不为null
            // 则调用用户自己的逻辑验证用户是否有效
            boolean b = tokenConfigService.checkLoginStatus();
            if (!b) {
                throw new GXBusinessException("登录状态已经失效,请重新登录!", HttpStatus.HTTP_NOT_AUTHORITATIVE);
            }
            return JSONUtil.toBean(o.toString(), Dict.class);
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
        // 2、将解码出来的token放入缓存
        set(ssoToken, expires);
        // 3、如果http中携带的的SSOToken不为null 则调用用户自己的逻辑验证用户是否有效
        boolean b = tokenConfigService.checkLoginStatus();
        if (!b) {
            throw new GXBusinessException("登录状态已经失效,请重新登录!", HttpStatus.HTTP_NOT_AUTHORITATIVE);
        }
        return Convert.convert(Dict.class, tokenData);
    }

    /**
     * 设置SSO票据
     *
     * @param key      关键词
     * @param ssoToken SSO票据
     * @param expires  过期时间
     */
    @SuppressWarnings("all")
    default boolean set(Dict ssoToken, int expires) {
        GXBaseCacheService cacheService = GXSpringContextUtils.getBean(GXBaseCacheService.class);
        GXTokenConfigService tokenConfigService = GXSpringContextUtils.getBean(GXTokenConfigService.class);
        assert tokenConfigService != null;
        assert cacheService != null;
        Long id = Optional.ofNullable(ssoToken.getLong(GXTokenConstant.TOKEN_USER_ID_FIELD_NAME)).orElse(ssoToken.getLong(GXTokenConstant.TOKEN_ADMIN_ID_FIELD_NAME));
        if (Objects.isNull(id)) {
            throw new GXBusinessException("token中未包含有效的id标识");
        }
        String tokenCacheKey = tokenConfigService.getTokenCacheKey(id, ssoToken);
        String cacheBucketName = tokenConfigService.getCacheBucketName();
        cacheService.setCache(cacheBucketName, tokenCacheKey, JSONUtil.toJsonStr(ssoToken), expires, TimeUnit.SECONDS);
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
        if (CollUtil.isEmpty(ssoToken)) {
            String tokenSecret = tokenConfigService.getTokenSecret();
            ssoToken = GXCurrentRequestContextUtils.getLoginCredentials(GXTokenConstant.TOKEN_NAME, tokenSecret);
        }
        Long id = Optional.ofNullable(ssoToken.getLong(GXTokenConstant.TOKEN_USER_ID_FIELD_NAME)).orElse(ssoToken.getLong(GXTokenConstant.TOKEN_ADMIN_ID_FIELD_NAME));
        if (Objects.isNull(id)) {
            throw new GXBusinessException("token中未包含有效的id标识");
        }
        String tokenCacheKey = tokenConfigService.getTokenCacheKey(id, ssoToken);
        String cacheBucketName = tokenConfigService.getCacheBucketName();
        Object o = cacheService.deleteCache(cacheBucketName, tokenCacheKey);
        return Objects.nonNull(o);
    }

    /**
     * 自定义验证缓存中的token与header或者Cookie中的token是否一致
     * 该验证逻辑会被拦截器自动调用
     *
     * @param cacheSSOToken  缓存中存储的token数据
     * @param cookieSSOToken 从header或者cookie中获取的token数据
     * @return boolean
     */
    default boolean verifyTokenConsistency(Dict cacheSSOToken, Dict cookieSSOToken) {
        // 验证 cookieSSOToken 与 cacheSSOToken 中的登录时间是否 不一致返回 false
        Long cookieLoginAt = Optional.ofNullable(cookieSSOToken.getLong("loginAt")).orElse(0L);
        Long cacheLoginAt = Optional.ofNullable(cacheSSOToken.getLong("loginAt")).orElse(1L);
        String activeProfile = GXCommonUtils.getActiveProfile();
        return CharSequenceUtil.equalsIgnoreCase(activeProfile, "local") || cookieLoginAt.equals(cacheLoginAt);
    }
}
