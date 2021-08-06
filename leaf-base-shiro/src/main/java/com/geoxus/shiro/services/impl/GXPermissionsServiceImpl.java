package com.geoxus.shiro.services.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import com.geoxus.shiro.entities.GXPermissionsEntity;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import com.geoxus.shiro.mapper.GXPermissionsMapper;
import com.geoxus.shiro.services.GXAdminPermissionsService;
import com.geoxus.shiro.services.GXPermissionsService;
import com.geoxus.shiro.services.GXRolePermissionsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class GXPermissionsServiceImpl extends ServiceImpl<GXPermissionsMapper, GXPermissionsEntity> implements GXPermissionsService<GXPermissionsEntity> {
    @Resource
    private GXAdminPermissionsService<GXAdminPermissionsEntity> adminPermissionsService;

    @Resource
    private GXRolePermissionsService<GXRolePermissionsEntity> rolePermissionsService;

    @Override
    public Set<String> getAdminAllPermissions(Long adminId) {
        Set<String> adminPermissions = adminPermissionsService.getPermissionsByAdminId(adminId);
        Set<String> rolePermissions = rolePermissionsService.getPermissionsByAdminId(adminId);
        final Collection<String> permissions = CollUtil.addAll(adminPermissions, rolePermissions);
        return new HashSet<>(permissions);
    }
}
