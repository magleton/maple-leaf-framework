package com.geoxus.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.datasource.service.impl.GXDBBaseServiceImpl;
import com.geoxus.shiro.dao.GXAdminRoleDao;
import com.geoxus.shiro.dto.res.GXAdminRoleResDto;
import com.geoxus.shiro.entities.GXAdminRolesEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;
import com.geoxus.shiro.service.GXAdminRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminRoleServiceImpl extends
        GXDBBaseServiceImpl<GXAdminRolesEntity, GXAdminRoleMapper, GXAdminRoleDao, GXAdminRoleResDto> implements GXAdminRoleService {
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
