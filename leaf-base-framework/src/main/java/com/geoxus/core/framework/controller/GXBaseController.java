package com.geoxus.core.framework.controller;

import com.geoxus.core.framework.util.GXHttpContextUtils;

public interface GXBaseController {
    /**
     * 从token中获取用户ID
     *
     * @param tokenName   token的名字
     * @param tokenIdName token中的字段表示
     * @return Long
     */
    default long getUserIdFromToken(String tokenName, String tokenIdName) {
        return GXHttpContextUtils.getUserIdFromToken(tokenName, tokenIdName);
    }
}
