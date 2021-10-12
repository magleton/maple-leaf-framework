package com.geoxus.shiro.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.shiro.dao.GXAdminRoleDao;
import com.geoxus.shiro.entities.GXAdminRolesEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;

import java.util.HashSet;
import java.util.Set;

public interface GXAdminRoleService extends com.geoxus.core.service.GXDBBaseService<GXAdminRolesEntity, GXAdminRoleMapper, GXAdminRoleDao>, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService {
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
    long create(GXAdminRolesEntity target, Dict param);
}
