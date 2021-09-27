package com.geoxus.shiro.service;

import com.geoxus.core.framework.service.GXBusinessService;
import com.geoxus.shiro.dao.GXPermissionsDao;
import com.geoxus.shiro.entities.GXPermissionsEntity;
import com.geoxus.shiro.mapper.GXPermissionsMapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface GXPermissionsService extends GXBusinessService<GXPermissionsEntity, GXPermissionsMapper, GXPermissionsDao> {
    /**
     * 获取管理员的所有权限列表
     * 权限包括:
     * <p>
     * 1、分配给角色的权限
     * 2、直接分配给管理员的权限
     *
     * @param adminId 为NULL是获取当前登录人的
     * @return Set
     */
    default Set<String> getAdminAllPermissions(Long adminId) {
        return new HashSet<>();
    }

    /**
     * 获取指定角色的权限ID集合
     *
     * @param roleId 角色ID
     * @return List
     */
    default List<Long> getRolePermissions(Long roleId) {
        return Collections.emptyList();
    }

    /**
     * 获取指定用户的权限ID集合
     *
     * @param adminId 管理员ID
     * @return
     */
    default List<Long> getAdminPermissions(long adminId) {
        return Collections.emptyList();
    }
}
