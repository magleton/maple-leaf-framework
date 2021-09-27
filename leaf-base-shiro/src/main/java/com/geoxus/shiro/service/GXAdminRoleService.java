package com.geoxus.shiro.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.service.GXBusinessService;
import com.geoxus.shiro.dao.GXAdminRoleDao;
import com.geoxus.shiro.entities.GXAdminRoleEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;

import java.util.HashSet;
import java.util.Set;

public interface GXAdminRoleService extends GXBusinessService<GXAdminRoleEntity, GXAdminRoleMapper, GXAdminRoleDao> {
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
    long create(GXAdminRoleEntity target, Dict param);
}
