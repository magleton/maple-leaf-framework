package com.geoxus.shiro.service;

import com.geoxus.shiro.dao.GXRolePermissionsDao;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import com.geoxus.shiro.mapper.GXRolePermissionsMapper;

import java.util.Set;

public interface GXRolePermissionsService extends com.geoxus.core.service.GXDBBaseService<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsDao>, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    Set<String> getPermissionsByAdminId(Long adminId);
}
