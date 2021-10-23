package com.geoxus.shiro.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.geoxus.shiro.dao.GXPermissionsDao;
import com.geoxus.shiro.entities.GXPermissionsEntity;
import com.geoxus.shiro.mapper.GXPermissionsMapper;
import com.geoxus.shiro.service.GXAdminPermissionsService;
import com.geoxus.shiro.service.GXPermissionsService;
import com.geoxus.shiro.service.GXRolePermissionsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class GXPermissionsServiceImpl extends com.geoxus.core.datasource.service.impl.GXDBBaseServiceImpl<GXPermissionsEntity, GXPermissionsMapper, GXPermissionsDao> implements GXPermissionsService, com.geoxus.core.datasource.service.GXValidateDBExistsService, com.geoxus.core.datasource.service.GXValidateDBUniqueService, com.geoxus.core.datasource.service.GXDBBaseService<GXPermissionsEntity, GXPermissionsMapper, GXPermissionsDao> {
    @Resource
    private GXAdminPermissionsService adminPermissionsService;

    @Resource
    private GXRolePermissionsService rolePermissionsService;

    @Override
    public Set<String> getAdminAllPermissions(Long adminId) {
        Set<String> adminPermissions = adminPermissionsService.getPermissionsByAdminId(adminId);
        Set<String> rolePermissions = rolePermissionsService.getPermissionsByAdminId(adminId);
        final Collection<String> permissions = CollUtil.addAll(adminPermissions, rolePermissions);
        return new HashSet<>(permissions);
    }
}
