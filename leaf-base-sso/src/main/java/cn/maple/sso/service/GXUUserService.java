package cn.maple.sso.service;

import cn.hutool.core.lang.Dict;

public interface GXUUserService {
    /**
     * 验证前端用户的Token是否有效
     *
     * @param token 用户token
     * @return Dict
     */
    default Dict verifyUserToken(String token) {
        return Dict.create();
    }

    /**
     * 账号登录
     *
     * @param loginParam 登录参数
     * @return 登录token
     */
    default String login(Dict loginParam) {
        return "";
    }

    /**
     * 通过用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return T
     */
    default Dict getUserByUserId(Long userId) {
        return null;
    }

    /**
     * 用户登出
     */
    default void loginOut() {

    }
}
