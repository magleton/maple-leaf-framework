package cn.maple.sso.service;

import cn.maple.core.framework.constant.GXTokenConstant;

/**
 * 用户可以实现该接口
 * 实现自己的业务配置
 */
public interface GXTokenConfigService {
    /**
     * 获取C端客户token的加解密key
     *
     * @return token加解密key
     */
    default String getUserTokenSecret() {
        return GXTokenConstant.USER_TOKEN_SECRET_KEY;
    }

    /**
     * 获取管理端token的加解密key
     *
     * @return token加解密key
     */
    default String getAdminTokenSecret() {
        return GXTokenConstant.ADMIN_TOKEN_SECRET_KEY;
    }

    /**
     * 获取通用token的加解密key
     *
     * @return token加解密key
     */
    default String getTokenSecret() {
        return GXTokenConstant.TOKEN_SECRET_KEY;
    }

    /**
     * 获取token的缓存存储桶
     *
     * @return 存储桶的名字
     */
    default String getCacheBucketName() {
        return "token_bucket";
    }

    /**
     * 检测当前用户的登录状态是否有效
     * 可以通过调用别的服务来判断
     *
     * @return 是否有效
     */
    default boolean checkLoginStatus() {
        return true;
    }

    /**
     * 获取token的缓存key的前缀
     *
     * @return 缓存key前缀
     */
    default String getTokenCachePrefix() {
        return "ssoTokenKey_";
    }
}
