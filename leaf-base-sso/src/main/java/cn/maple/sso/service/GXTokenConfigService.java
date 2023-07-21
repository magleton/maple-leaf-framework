package cn.maple.sso.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;

/**
 * 用户可以实现该接口
 * 实现自己的业务配置
 */
public interface GXTokenConfigService {
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
        String appName = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class, "");
        if (CharSequenceUtil.isNotBlank(appName)) {
            return CharSequenceUtil.format("token_bucket:{}", appName);
        }
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

    /**
     * 获取token的缓存
     *
     * @param userId    用户id
     * @param extraData 额外参数 根据业务需要自信传递
     * @return token的cache缓存
     */
    default String getTokenCacheKey(Long userId, Dict extraData) {
        throw new GXBusinessException("请实现缓存的cache键方法!");
    }

    /**
     * 获取业务有效的token
     * 该token可以来自于缓存或者别的服务
     * 默认为当前请求中的token解码之后的数据
     *
     * @param requestToken 当前请求中token字符串解码之后的数据
     * @return Dict
     */
    default Dict getEfficaciousToken(Dict requestToken) {
        return requestToken;
    }

    /**
     * 验证token的有效性
     * 验证规则可以调用别的服务
     * 亦可以自己验证
     * 自身验证可以通过redis的token缓存key+tokenSecret来进行验证解密 并验证是否有效
     * 减少服务间的通信
     *
     * @return true 有效 ; false 无效
     */
    default boolean verifyTokenEffectiveness() {
        return Boolean.TRUE;
    }
}
