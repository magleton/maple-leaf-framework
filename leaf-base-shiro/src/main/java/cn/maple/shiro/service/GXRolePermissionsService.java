package cn.maple.shiro.service;

import cn.maple.core.datasource.service.GXDBBaseService;
import cn.maple.shiro.dao.GXRolePermissionsDao;
import cn.maple.shiro.dto.res.GXRolePermissionsResDto;
import cn.maple.shiro.entities.GXRolePermissionsEntity;
import cn.maple.shiro.mapper.GXRolePermissionsMapper;

import java.util.Set;

public interface GXRolePermissionsService extends
        GXDBBaseService<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsDao, GXRolePermissionsResDto> {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    Set<String> getPermissionsByAdminId(Long adminId);
}
