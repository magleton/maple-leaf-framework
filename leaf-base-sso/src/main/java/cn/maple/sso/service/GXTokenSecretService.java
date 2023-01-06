package cn.maple.sso.service;

import cn.maple.core.framework.constant.GXTokenConstant;

public interface GXTokenSecretService {
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
}
