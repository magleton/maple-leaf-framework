package com.geoxus.shiro.service.impl;

import com.geoxus.core.datasource.service.impl.GXDBBaseServiceImpl;
import com.geoxus.shiro.dao.GXRolePermissionsDao;
import com.geoxus.shiro.dto.res.GXRolePermissionsResDto;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import com.geoxus.shiro.mapper.GXRolePermissionsMapper;
import com.geoxus.shiro.service.GXRolePermissionsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXRolePermissionsServiceImpl
        extends GXDBBaseServiceImpl<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsDao, GXRolePermissionsResDto>
        implements GXRolePermissionsService {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    @Override
    public Set<String> getPermissionsByAdminId(Long adminId) {
        return getBaseMapper().getPermissionsByAdminId(adminId);
    }
}
