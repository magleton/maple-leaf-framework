package com.geoxus.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.shiro.dao.GXAdminRoleDao;
import com.geoxus.shiro.entities.GXAdminRolesEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;
import com.geoxus.shiro.service.GXAdminRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminRoleServiceImpl extends com.geoxus.core.datasource.service.impl.GXDBBaseServiceImpl<GXAdminRolesEntity, GXAdminRoleMapper, GXAdminRoleDao> implements GXAdminRoleService, com.geoxus.core.datasource.service.GXValidateDBExistsService, com.geoxus.core.datasource.service.GXValidateDBUniqueService, com.geoxus.core.datasource.service.GXDBBaseService<GXAdminRolesEntity, GXAdminRoleMapper, GXAdminRoleDao> {
    @Override
    public Set<String> getAdminRoles(Long adminId) {
        final Dict condition = Dict.create().set("admin_id", adminId);
        return getBaseMapper().getAdminRoles(condition);
    }

    public long create(GXAdminRolesEntity target, Dict param) {
        baseDao.save(target);
        return target.getId();
    }
}
