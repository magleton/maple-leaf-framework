package cn.maple.core.framework.service;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.exception.GXBusinessException;

/**
 * 续约Token
 * Token无感刷新的Service
 * 该功能需要前端配合
 */
public interface GXRenewalTokenService {
    /**
     * 验证token是否需要刷新
     * 如果需要刷新 当前请求的响应头中会新增Renewal-Token头
     *
     * @return 需要刷新 true 不需要刷新 false
     */
    default boolean renewalToken() {
        throw new GXBusinessException("请自定义实现Token无感刷新的逻辑");
    }

    /**
     * 重新获取Token
     *
     * @param param 自定义参数
     * @return 新的token
     */
    default String refreshToken(Dict param) {
        throw new GXBusinessException("请自定义实现Token无感刷新的逻辑");
    }
}
