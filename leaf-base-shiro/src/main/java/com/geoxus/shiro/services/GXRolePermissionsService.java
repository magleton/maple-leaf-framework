package com.geoxus.shiro.services;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;

import java.util.Set;

public interface GXRolePermissionsService<T extends GXRolePermissionsEntity> extends GXBusinessService<T> {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    Set<String> getPermissionsByAdminId(Long adminId);
}
