package com.geoxus.shiro.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.shiro.entities.GXAdminRoleEntity;

import java.util.HashSet;
import java.util.Set;

public interface GXAdminRoleService<T extends GXAdminRoleEntity> extends GXBusinessService<T, Dict> {
    /**
     * 获取当前人的角色列表
     *
     * @param adminId 为NULL是获取当前登录人的
     * @return Dict
     */
    default Set<String> getAdminRoles(Long adminId) {
        return new HashSet<>(0);
    }

    /**
     * 创建数据
     *
     * @param target 目标实体
     * @param param  额外参数
     * @return long
     */
    long create(T target, Dict param);
}
