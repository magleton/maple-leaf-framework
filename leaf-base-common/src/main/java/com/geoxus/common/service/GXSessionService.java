package com.geoxus.common.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.util.GXBaseCommonUtil;

public interface GXSessionService {
    /**
     * 获取当前登录用户的ID
     *
     * @return Long
     */
    default Long currentSessionUserId() {
        return GXBaseCommonUtil.getCurrentSessionUserId();
    }

    /**
     * 获取当前管理员的角色信息
     *
     * @param currentAdminId 当前用户ID
     * @return Dict
     */
    default Dict adminRoles(Long currentAdminId) {
        return Dict.create();
    }
}
