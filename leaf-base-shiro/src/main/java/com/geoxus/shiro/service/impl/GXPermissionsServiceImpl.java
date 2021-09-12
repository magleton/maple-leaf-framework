package com.geoxus.shiro.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.common.service.impl.GXBusinessServiceImpl;
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
public class GXPermissionsServiceImpl extends GXBusinessServiceImpl<GXPermissionsEntity, GXPermissionsMapper, GXPermissionsDao> implements GXPermissionsService {
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
