package com.geoxus.shiro.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.util.GXBaseCommonUtil;

import java.util.Set;

public interface GXShiroService {
    /**
     * 获取当前登录管理员的所有权限列表
     * 权限包括:
     * <p>
     * 1、分配给角色的权限
     * 2、直接分配的权限
     *
     * @param adminId 用户ID
     * @return Set
     */
    Set<String> getAdminAllPermissions(Long adminId);

    /**
     * 获取当前登录管理员的角色列表
     *
     * @param adminId 用户ID
     * @return Set
     */
    Set<String> getAdminRoles(Long adminId);

    /**
     * 查询Admin的详细信息
     *
     * @param adminId 管理员ID
     * @return SAdminEntity
     */
    Dict getAdminById(Long adminId);

    /**
     * 判断是否时超级管理员
     *
     * @param adminData admin的信息
     * @return boolean
     */
    boolean isSuperAdmin(Dict adminData);

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
