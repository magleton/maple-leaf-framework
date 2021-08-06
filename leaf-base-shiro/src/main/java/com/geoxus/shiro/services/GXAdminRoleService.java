package com.geoxus.shiro.services;

import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.shiro.entities.GXAdminRoleEntity;

import java.util.HashSet;
import java.util.Set;

public interface GXAdminRoleService<T extends GXAdminRoleEntity> extends GXBusinessService<T> {
    /**
     * 获取当前人的角色列表
     *
     * @param adminId 为NULL是获取当前登录人的
     * @return Dict
     */
    default Set<String> getAdminRoles(Long adminId) {
        return new HashSet<>(0);
    }
}
