package com.geoxus.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.common.util.GXHttpContextUtils;

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

    /**
     * 从token中获取用户ID
     *
     * @param riPage 分页对象
     * @return Long
     */
    default <R> GXPaginationProtocol<R> generateGXPaginationProtocol(IPage<R> riPage) {
        return new GXPaginationProtocol<>(riPage.getRecords(), riPage.getTotal(), riPage.getSize(), riPage.getCurrent());
    }
}
