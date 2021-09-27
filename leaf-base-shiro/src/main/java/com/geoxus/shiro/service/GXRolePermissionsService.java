package com.geoxus.shiro.service;

import com.geoxus.core.framework.service.GXBusinessService;
import com.geoxus.shiro.dao.GXRolePermissionsDao;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import com.geoxus.shiro.mapper.GXRolePermissionsMapper;

import java.util.Set;

public interface GXRolePermissionsService extends GXBusinessService<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsDao> {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    Set<String> getPermissionsByAdminId(Long adminId);
}
