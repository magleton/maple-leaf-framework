package com.geoxus.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.common.service.impl.GXBusinessServiceImpl;
import com.geoxus.shiro.dao.GXAdminRoleDao;
import com.geoxus.shiro.entities.GXAdminRoleEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;
import com.geoxus.shiro.service.GXAdminRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminRoleServiceImpl extends GXBusinessServiceImpl<GXAdminRoleEntity, GXAdminRoleMapper, GXAdminRoleDao, Dict> implements GXAdminRoleService {
    @Override
    public Set<String> getAdminRoles(Long adminId) {
        final Dict condition = Dict.create().set("admin_id", adminId);
        return getBaseMapper().getAdminRoles(condition);
    }

    public long create(GXAdminRoleEntity target, Dict param) {
        baseDao.save(target);
        return target.getId();
    }
}
