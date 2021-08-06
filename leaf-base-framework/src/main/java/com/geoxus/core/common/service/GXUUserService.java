package com.geoxus.core.common.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.entity.GXUUserEntity;
import com.geoxus.core.common.validator.GXValidateDBExists;

public interface GXUUserService<T extends GXUUserEntity> extends GXBusinessService<T>, GXValidateDBExists {
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
     * 用户登出
     */
    default void loginOut() {

    }
}
